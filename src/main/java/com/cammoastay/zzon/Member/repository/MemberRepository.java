package com.cammoastay.zzon.Member.repository;

import com.cammoastay.zzon.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByUserLoginId(String userLoginId);
}
