package com.cammoastay.zzon.User.repository;

import com.cammoastay.zzon.User.entity.UserRefresh;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<UserRefresh, Long> {
    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);
}
