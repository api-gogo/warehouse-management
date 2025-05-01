package com.ohgiraffers.warehousemanagement.wms.inventory.model;

public enum InventoryLogTransactionType {
    STORAGE("입고"),
    SHIPMENT("출고"),
    RETURN("출고반품"),
    ADJUSTMENT("재고수정");

    private String transactionType;

    InventoryLogTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
