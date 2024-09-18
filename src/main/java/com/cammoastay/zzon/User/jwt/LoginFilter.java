package com.cammoastay.zzon.User.jwt;

import com.cammoastay.zzon.User.dto.MemberDetails;
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
import java.util.Iterator;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
        private final AuthenticationManager authenticationManager;
        private final JWTUtil jwtUtil;

        public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
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

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
        }

        //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

            System.out.println("로그인성공");
            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
            
            //id저장
            String userLoginId = memberDetails.getUsername(); //username = userLoginId
            
            //권한저장
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();

            String role = auth.getAuthority();

            //토큰 생성
            String access = jwtUtil.createJwt("access", userLoginId, role, 60*60*10L);
            String refresh = jwtUtil.createJwt("refresh",userLoginId, role, 60*60*10L);

            response.setHeader("access", access);
            response.addCookie(createCookie("refresh", refresh));
            response.setStatus(HttpStatus.OK.value());
        }

        //로그인 실패시 실행하는 메소드
        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
            System.out.println("로그인실패");
            response.setStatus(401);

        }
}
