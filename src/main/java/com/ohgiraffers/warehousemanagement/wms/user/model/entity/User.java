package com.ohgiraffers.warehousemanagement.wms.user.model.entity;

import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserPart;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserRole;
import com.ohgiraffers.warehousemanagement.wms.user.model.common.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_code", length = 50, nullable = false, unique = true)
    private String userCode;

    @Column(name = "user_pass", length = 255, nullable = false)
    private String userPass;

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;

    @Column(name = "user_email", length = 100, nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_phone", length = 50, nullable = false, unique = true)
    private String userPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_part", length = 50, nullable = false)
    private UserPart userPart;

    // 사원, 매니저, 관리자
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    // 재직중, 휴직중, 퇴사
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @Column(name = "user_created_at", nullable = false)
    private LocalDateTime userCreatedAt;

    // 생성될 때는 등록 X, 업데이트 될 때만
    @Column(name = "user_updated_at")
    private LocalDateTime userUpdatedAt;

    @Column(name = "user_deleted_at")
    private LocalDateTime userDeletedAt;

    protected User() {}

    public User(String userCode, String userPass, String userName, String userEmail, String userPhone, UserPart userPart) {
        this.userCode = userCode;
        this.userPass = userPass;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPart = userPart;
        this.userRole = UserRole.사원;
        this.userStatus = UserStatus.승인대기;
        this.userCreatedAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getUserPass() {
        return userPass;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public UserPart getUserPart() {
        return userPart;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public LocalDateTime getUserCreatedAt() {
        return userCreatedAt;
    }

    public LocalDateTime getUserUpdatedAt() {
        return userUpdatedAt;
    }

    public LocalDateTime getUserDeletedAt() {
        return userDeletedAt;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserPart(UserPart userPart) {
        this.userPart = userPart;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserUpdatedAt(LocalDateTime userUpdatedAt) {
        this.userUpdatedAt = userUpdatedAt;
    }

    public void setUserDeletedAt(LocalDateTime userDeletedAt) {
        this.userDeletedAt = userDeletedAt;
    }
}
