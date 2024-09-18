package com.cammoastay.zzon.User.controller;

import com.cammoastay.zzon.User.entity.UserRefresh;
import com.cammoastay.zzon.User.jwt.JWTUtil;
import com.cammoastay.zzon.User.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueController(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {

        jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

        //response status code
        return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

                //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            return new ResponseEntity<>("refresh token not found", HttpStatus.BAD_REQUEST);
        }

        String userLoginId = jwtUtil.getUserLoginId(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", userLoginId, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", userLoginId, role, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addUserRefresh(userLoginId, newRefresh, 86400000L);
        //response
            response.setHeader("access", newAccess);
            response.addCookie(createCookie("refresh", newRefresh));

            return new ResponseEntity<>(HttpStatus.OK);
        }

    private void addUserRefresh(String userLoginId, String refresh, Long expiredMs){
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        UserRefresh userRefresh = new UserRefresh();
        userRefresh.setUserLoginId(userLoginId);
        userRefresh.setRefresh(refresh);
        userRefresh.setExpiration(date.toString());

        refreshRepository.save(userRefresh);

    }

    private Cookie createCookie(String key, String value) {

       Cookie cookie = new Cookie(key, value);
       cookie.setMaxAge(24*60*60);
       //cookie.setSecure(true);
       // cookie.setPath("/");
       // cookie.setHttpOnly(true);

        return cookie;
        }
}
