package com.ohgiraffers.warehousemanagement.wms.returning.model;

public enum ReturnShipmentStatus {

    RETURN_WAITING("반품 대기"),
    RETURN_APPROVED("반품 승인"),
    RETURN_REJECTED("반품 거절"),
    RETURN_COMPLETED("반품 완료");

    private String returnShipmentStatus;

    ReturnShipmentStatus(String returnShipmentStatus) {
        this.returnShipmentStatus = returnShipmentStatus;
    }

    public String getReturnShipmentStatus() {
        return returnShipmentStatus;
    }

    public void setReturnShipmentStatus(String returnShipmentStatus) {
        this.returnShipmentStatus = returnShipmentStatus;
    }

    @Override
    public String toString() {
        return "반품상태 {" /*+
                "returnShipmentStatus='"*/ + returnShipmentStatus + '\'' +
                '}';
    }
}
