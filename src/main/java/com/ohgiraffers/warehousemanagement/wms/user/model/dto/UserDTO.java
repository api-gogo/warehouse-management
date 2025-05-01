package com.ohgiraffers.warehousemanagement.wms.user.model.dto;

import java.time.LocalDateTime;

public class UserDTO {

    private Long userId;
    private String userCode;
    private String userPass;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userPart;
    private String userRole;
    private String userStatus;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;
    private LocalDateTime userDeletedAt;

    public UserDTO() {}

    public UserDTO(Long userId, String userCode, String userPass, String userName, String userEmail, String userPhone, String userPart, String userRole, String userStatus, LocalDateTime userCreatedAt, LocalDateTime userUpdatedAt, LocalDateTime userDeletedAt) {
        this.userId = userId;
        this.userCode = userCode;
        this.userPass = userPass;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPart = userPart;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.userCreatedAt = userCreatedAt;
        this.userUpdatedAt = userUpdatedAt;
        this.userDeletedAt = userDeletedAt;
    }

    public UserDTO(Long userId, String userCode, String userName, String userEmail, String userPhone, String userPart, String userRole, String userStatus, LocalDateTime userCreatedAt, LocalDateTime userUpdatedAt, LocalDateTime userDeletedAt) {
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPart = userPart;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.userCreatedAt = userCreatedAt;
        this.userUpdatedAt = userUpdatedAt;
        this.userDeletedAt = userDeletedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPart() {
        return userPart;
    }

    public void setUserPart(String userPart) {
        this.userPart = userPart;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(LocalDateTime userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

    public LocalDateTime getUserUpdatedAt() {
        return userUpdatedAt;
    }

    public void setUserUpdatedAt(LocalDateTime userUpdatedAt) {
        this.userUpdatedAt = userUpdatedAt;
    }

    public LocalDateTime getUserDeletedAt() {
        return userDeletedAt;
    }

    public void setUserDeletedAt(LocalDateTime userDeletedAt) {
        this.userDeletedAt = userDeletedAt;
    }
}
