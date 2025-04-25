package com.ohgiraffers.warehousemanagement.wms.user.model.dto;

public class LoginUserDTO {

    private Integer userId;
    private String userCode;
    private String userPass;
    private String userName;
    private String userPart;
    private String userRole;
    private String userStatus;

    public LoginUserDTO() {}

    public LoginUserDTO(Integer userId, String userCode, String userPass, String userName, String userPart, String userRole, String userStatus) {
        this.userId = userId;
        this.userCode = userCode;
        this.userPass = userPass;
        this.userName = userName;
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
