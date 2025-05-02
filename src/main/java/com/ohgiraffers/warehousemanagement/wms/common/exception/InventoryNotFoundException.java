package com.ohgiraffers.warehousemanagement.wms.common.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
