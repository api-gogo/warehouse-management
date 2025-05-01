package com.ohgiraffers.warehousemanagement.wms.returning;

public enum ReturningShipmentCause {
    EXPIRED("유통기한 만료"),
    DAMAGED_OR_DEFECTIVE("손상 또는 결함");


    private String returnShipmentCause;

    ReturningShipmentCause(String returnShipmentCause) {
        this.returnShipmentCause = returnShipmentCause;
    }

    public String getReturnShipmentCause() {
        return returnShipmentCause;
    }

    public void setReturnShipmentCause(String returnShipmentCause) {
        this.returnShipmentCause = returnShipmentCause;
    }

    @Override
    public String toString() {
        return "반품 사유{" /*+
                "returnShipmentCause='" */+ returnShipmentCause + '\'' +
                '}';
    }
}
