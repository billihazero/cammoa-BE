package com.cammoastay.zzon.userMyPage;

import com.cammoastay.zzon.user.entity.Member;
import com.cammoastay.zzon.user.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMyPageController {

    private final JWTUtil jwtUtil;
    private final UserMyPageService userMyPageService;

    public UserMyPageController(JWTUtil jwtUtil, UserMyPageService userMyPageService) {
        this.jwtUtil = jwtUtil;
        this.userMyPageService = userMyPageService;
    }

//    @GetMapping("/memberget")
//    public ResponseEntity<Member> getMyPage(HttpServletRequest request) {
//
////        // 쿠키에서 accessToken 추출
////        String access = null;
////        Cookie[] cookies = request.getCookies();
////        if (cookies != null) {
////            for (Cookie cookie : cookies) {
////                if (cookie.getName().equals("access")) {
////                    access = cookie.getValue();
////                    break;
////                }
////            }
////        }
////        Long userId = jwtUtil.getUserId(access);
////
////
////    }
}
