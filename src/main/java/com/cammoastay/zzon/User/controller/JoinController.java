package com.cammoastay.zzon.User.controller;

import com.cammoastay.zzon.User.dto.JoinDTO;
import com.cammoastay.zzon.User.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinProcess(@RequestBody JoinDTO joinDTO){

        Map<String, String> response = new HashMap<>();

        // 아이디가 유효할 경우 회원가입 진행
        joinService.joinProcess(joinDTO);
        response.put("message", "회원가입이 성공적으로 처리되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    @PostMapping("/checkid")
    public ResponseEntity<Map<String, String>> checkId(@RequestBody JoinDTO joinDTO){
        boolean isIdValid = joinService.checkId(joinDTO.userLoginId());

        Map<String, String> response = new HashMap<>();

        if(isIdValid){
            response.put("message", "이미 사용중인 아이디 입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
        }
        return ResponseEntity.ok(response);
    }

}
