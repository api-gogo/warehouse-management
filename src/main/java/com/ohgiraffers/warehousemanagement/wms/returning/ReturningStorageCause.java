package com.ohgiraffers.warehousemanagement.wms.returning;

public enum ReturningStorageCause {

    EXPIRED("유통기한 만료"),
    DAMAGED_OR_DEFECTIVE("손상 또는 결함"),
    QUANTITY_DISCREPANCY("수량 불일치"),
    PRODUCT_DISCREPANCY("상품 정보 불일치");

    private String returnStorageCause;

    ReturningStorageCause(String returnStorageCause) {this.returnStorageCause = returnStorageCause;}

    public String getReturnStorageCause() {
        return returnStorageCause;
    }

    public void setReturnStorageCause(String returnStorageCause) {
        this.returnStorageCause = returnStorageCause;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
