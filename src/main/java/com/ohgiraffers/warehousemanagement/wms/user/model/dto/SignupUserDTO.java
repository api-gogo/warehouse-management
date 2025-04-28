package com.ohgiraffers.warehousemanagement.wms.user.model.dto;

public class SignupUserDTO {

    private String userCode;
    private String userPass;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userPart;
    private String userRole;

    public SignupUserDTO() {}

    public SignupUserDTO(String userCode, String userPass, String userName, String userEmail, String userPhone, String userPart, String userRole) {
        this.userCode = userCode;
        this.userPass = userPass;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPart = userPart;
        this.userRole = userRole;
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
}
