package com.cammoastay.zzon.user.jwt;

import com.cammoastay.zzon.user.dto.MemberDetails;
import com.cammoastay.zzon.user.entity.UserRefresh;
import com.cammoastay.zzon.user.repository.RefreshRepository;
import com.cammoastay.zzon.user.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
        private final AuthenticationManager authenticationManager;
        private final JWTUtil jwtUtil;
        private final RefreshRepository refreshRepository;
        private final TokenService tokenService;

        public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository, TokenService tokenService) {

            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
            this.refreshRepository = refreshRepository;
            this.tokenService = tokenService;
            setFilterProcessesUrl("/api/v1/login");
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            try {
                // JSON 요청 본문을 Map으로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);

                // Map에서 userLoginId와 userPasswd 추출
                String userLoginId = requestBody.get("userLoginId");
                String userPasswd = requestBody.get("userPasswd");

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userLoginId, userPasswd, null);

                // token 검증을 위한 AuthenticationManager로 전달
                return authenticationManager.authenticate(authToken);
                
            } catch (IOException e) {
                throw new RuntimeException("로그인 실패", e);
            }


        }

        private void addUserRefresh(String userLoginId, String refresh, Long expiredMs){
            Date date = new Date(System.currentTimeMillis() + expiredMs);

            UserRefresh userRefresh = new UserRefresh();
            userRefresh.setUserLoginId(userLoginId);
            userRefresh.setRefresh(refresh);
            userRefresh.setExpiration(date.toString());

            //db에 저장
            refreshRepository.save(userRefresh);
        }

        //access cookie
        private Cookie createAccessCookie(String key, String value) {

            Cookie cookie = new Cookie(key, value);
            
            cookie.setMaxAge(60*60); //1시간
            cookie.setSecure(true);
            cookie.setPath("/");

            return cookie;
        }


        //refresh cookie
        private Cookie createCookie(String key, String value) {

            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(24*60*60);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setHttpOnly(true);

            return cookie;
            }


        //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

            System.out.println("로그인성공");
            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
            
            //id저장
            Long userId = memberDetails.getUserId();
            String userLoginId = memberDetails.getUsername(); //username = userLoginId
            
            //권한저장
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();

            String role = auth.getAuthority();

            //토큰 생성
            String access = jwtUtil.createJwt("access", userId, userLoginId, role, 3600000L); //1시간
            String refresh = jwtUtil.createJwt("refresh",userId, userLoginId, role, 86400000L); //하루

            //db저장
            addUserRefresh(userLoginId, refresh,86400000L);

            //캐시저장
            tokenService.cacheUserRefresh(userLoginId, refresh,86400000L);

            response.addCookie(createAccessCookie("access", access));
            response.addCookie(createCookie("refresh", refresh));
            response.setStatus(HttpStatus.OK.value());

            System.out.println("로그인성공!" +userId + "=" + userLoginId);
        }

        //로그인 실패시 실행하는 메소드
        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
            System.out.println("로그인실패");
            response.setStatus(401);

        }
}
