package com.cammoastay.zzon.User.repository;

import com.cammoastay.zzon.User.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUserLoginId( String userLoginId);
    boolean existsByUserLoginId( String userLoginId);

}
