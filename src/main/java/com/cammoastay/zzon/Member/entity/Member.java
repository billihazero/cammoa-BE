package com.cammoastay.zzon.Member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String userLoginId;

    private String userPasswd;
    private String userPhone;
    private String userName;
    private String userNickname;
    private String userEmail;
    private String userRole;
    private Boolean isDeleted = false;



}
