package com.ohgiraffers.warehousemanagement.wms.user.model.common;

public enum UserRole {
    사원("사원"),
    매니저("매니저"),
    관리자("관리자");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
