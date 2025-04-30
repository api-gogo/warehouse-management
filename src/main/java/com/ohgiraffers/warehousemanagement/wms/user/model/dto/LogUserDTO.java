package com.ohgiraffers.warehousemanagement.wms.user.model.dto;

public class LogUserDTO {

    private Integer userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userPart;
    private String userRole;
    private String userStatus;

    public LogUserDTO() {}

    public LogUserDTO(Integer userId, String userName, String userEmail, String userPhone, String userPart, String userRole, String userStatus) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPart = userPart;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
