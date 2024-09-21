package com.cammoastay.zzon.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserRefresh {

    public UserRefresh(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTokenId;

    private String userLoginId;
    private String refresh;

    //만료시간
    private String expiration;

}
