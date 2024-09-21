package com.cammoastay.zzon.user.repository;

import com.cammoastay.zzon.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUserLoginId( String userLoginId);
    boolean existsByUserLoginId( String userLoginId);

}
