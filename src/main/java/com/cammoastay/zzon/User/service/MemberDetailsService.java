package com.cammoastay.zzon.User.service;

import com.cammoastay.zzon.User.dto.MemberDetails;
import com.cammoastay.zzon.User.entity.Member;
import com.cammoastay.zzon.User.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userLoginId) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByUserLoginId(userLoginId);

        //존재하는 사용자라면
        if (memberData != null) {
            return new MemberDetails(memberData);
        }
        // 존재하지 않으면 UsernameNotFoundException을 던짐
        throw new UsernameNotFoundException("해당 id가 존재하지 않습니다.");
    }
}
