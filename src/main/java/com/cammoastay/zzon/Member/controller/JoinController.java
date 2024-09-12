package com.cammoastay.zzon.Member.controller;

import com.cammoastay.zzon.Member.dto.JoinDTO;
import com.cammoastay.zzon.Member.service.JoinService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/memberjoin")
    public String joinProcess(@RequestBody JoinDTO joinDTO){
        joinService.joinProcess(joinDTO);

        return "join ok";
    }

    @GetMapping("/idcheck")
    public String idcheck(@RequestParam String userLoginId){
        boolean isAvailable = joinService.isIdAvailable(userLoginId);

        if(isAvailable){
            return "idcheck ok";
        }

        return "idcheck fail";
    }

}
