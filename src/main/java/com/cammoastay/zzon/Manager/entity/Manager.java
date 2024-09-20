package com.cammoastay.zzon.Manager.entity;

import jakarta.persistence.*;

@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;

    private String managerLoginId;
    private String managerPasswd;
    private String managerName;
    private String managerPhone;
    private String managerEmail;
    private String businessNumber;
    private String campName;
    private String managerRole;
    private Boolean isDeleted = false;

}
