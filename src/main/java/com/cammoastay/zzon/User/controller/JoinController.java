package com.cammoastay.zzon.User.controller;

import com.cammoastay.zzon.User.dto.JoinDTO;
import com.cammoastay.zzon.User.service.JoinService;
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


}
