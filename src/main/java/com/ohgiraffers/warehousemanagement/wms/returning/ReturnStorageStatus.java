package com.ohgiraffers.warehousemanagement.wms.returning;

public enum ReturnStorageStatus {
    RETURN_WAITING("반품 대기"),
    RETURN_APPROVE("반품 승인"),
    RETURN_REJECTED("반품 거절"),
    RETURN_COMPLETED("반품 완료");

    private String returnStorageStatus;

    ReturnStorageStatus(String returnStorageStatus) {
        this.returnStorageStatus = returnStorageStatus;
    }

    public String getReturnStorageStatus() {
        return returnStorageStatus;
    }

    public void setReturnStorageStatus(String returnStorageStatus) {
        this.returnStorageStatus = returnStorageStatus;
    }

    @Override
    public String toString() {
        return "ReturnStorageStatus{" +
                "returnStorageStatus='" + returnStorageStatus + '\'' +
                '}';
    }
}

