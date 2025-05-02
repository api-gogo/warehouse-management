package com.ohgiraffers.warehousemanagement.wms.common.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(String message) {
        super(message);
    }
}
