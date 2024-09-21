package com.cammoastay.zzon.User.jwt;

import com.cammoastay.zzon.User.dto.MemberDetails;
import com.cammoastay.zzon.User.entity.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // 회원가입과 로그인 경로를 제외
        return path.startsWith("/api/v1/join") || path.startsWith("/api/v1/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //access토큰의 유효성 검증

        //refreshtoken cookie에 담기
        String access = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    access = cookie.getValue();
                }
            }
        }

        if (access == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //access토큰 만료 여부 확인
        //만료되었다면 다음 필터로 넘기지 x
        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {

            PrintWriter writer = response.getWriter();
            writer.print("accessToken123");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(access);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("유효하지 않은 token입니다.");

            //response status code
            //만료되었다면, front단과 협의하여 code를 결정한다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰에서 loginId와 role 획득
        String userLoginId = jwtUtil.getUserLoginId(access);
        String role = jwtUtil.getRole(access);

        //토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(userLoginId, null, List.of(() -> role));
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}
