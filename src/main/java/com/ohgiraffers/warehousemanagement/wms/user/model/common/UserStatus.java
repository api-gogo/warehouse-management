package com.ohgiraffers.warehousemanagement.wms.user.model.common;

public enum UserStatus {
    승인대기("승인대기"),
    승인거부("승인거부"),
    재직중("재직중"),
    휴직중("휴직중"),
    퇴사("퇴사");

    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
