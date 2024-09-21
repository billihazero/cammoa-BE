package com.cammoastay.zzon.userMyPage;

import com.cammoastay.zzon.user.entity.Member;
import com.cammoastay.zzon.user.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserMyPageController {

    private final UserMyPageService userMyPageService;

    public UserMyPageController(UserMyPageService userMyPageService) {
        this.userMyPageService = userMyPageService;
    }

    @GetMapping("/memberget")
    public ResponseEntity<?> getMyPage(HttpServletRequest request) {
        //SecurityContextHolder에서 현재 인증된 사용자 정보 가져오기
        //token에서 파싱된 userLoginId
        String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();

        // userLoginId를 사용해 회원 정보 조회
        Member memberData = userMyPageService.getMyPage(userLoginId);

        return ResponseEntity.ok(memberData);


    }
}
