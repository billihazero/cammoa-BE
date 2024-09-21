package com.cammoastay.zzon.userMyPage;

import com.cammoastay.zzon.user.entity.Member;
import com.cammoastay.zzon.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class UserMyPageService {

    private final MemberRepository memberRepository;

    public UserMyPageService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMyPage(String userLoginId) {
        Member memberData =  memberRepository.findByUserLoginId(userLoginId);

        return memberData;
    }
}
