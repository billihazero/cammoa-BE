package com.cammoastay.zzon.Member.service;

import com.cammoastay.zzon.Member.dto.JoinDTO;
import com.cammoastay.zzon.Member.entity.Member;
import com.cammoastay.zzon.Member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public void joinProcess(JoinDTO joinDTO) {
        String userLoginId = joinDTO.userLoginId();
        String userPasswd = joinDTO.userPasswd();
        String userPhone = joinDTO.userPhone();

        String userName = joinDTO.userName();
        String userNickname = joinDTO.userNickname();
        String userEmail = joinDTO.userEmail();

        Member member = new Member();

        member.setUserLoginId(userLoginId);
        member.setUserPasswd(bCryptPasswordEncoder.encode(userPasswd));
        member.setUserPhone(userPhone);

        member.setUserName(userName);
        member.setUserNickname(userNickname);
        member.setUserEmail(userEmail);

        member.setUserRole("ROLE_USER");

        memberRepository.save(member);
    }

    public boolean isIdAvailable(String userLoginId) {
        Boolean isIdExist = memberRepository.existsByUserLoginId(userLoginId);
        
        //사용가능한 id라면 true(1)를 출력
        return !isIdExist;
    }
}
