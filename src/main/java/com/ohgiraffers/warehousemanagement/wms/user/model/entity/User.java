package com.ohgiraffers.warehousemanagement.wms.user.model.entity;

import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_code", length = 50, nullable = false, unique = true)
    private String userCode;

    @Column(name = "user_pass", length = 255, nullable = false)
    private String userPass;

    @Column(name = "user_phone", length = 50, nullable = false)
    private String userPhone;

    @Column(name = "user_part", length = 50, nullable = false)
    private String userPart;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Column(name = "user_status", length = 50, nullable = false)
    private String userStatus;

    @Column(name = "user_created_at", nullable = false)
    private LocalDateTime userCreatedAt;

    @Column(name = "user_updated_at")
    private LocalDateTime userUpdatedAt;

    @Column(name = "user_deleted_at")
    private LocalDateTime userDeletedAt;
}
