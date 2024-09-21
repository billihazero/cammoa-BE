package com.cammoastay.zzon.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, name="user_login_id")
    private String userLoginId;

    @Column(name = "user_passwd")
    private String userPasswd;
    private String userPhone;
    private String userName;
    private String userNickname;
    private String userEmail;
    private String userRole;
    private Boolean isDeleted = false;



}
