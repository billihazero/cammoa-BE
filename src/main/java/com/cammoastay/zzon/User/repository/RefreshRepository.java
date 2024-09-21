package com.cammoastay.zzon.User.repository;

import com.cammoastay.zzon.User.entity.UserRefresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<UserRefresh, Long> {

    @Transactional
    void deleteByRefresh(String refresh);
}
