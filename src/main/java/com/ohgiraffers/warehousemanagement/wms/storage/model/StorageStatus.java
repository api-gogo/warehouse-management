package com.ohgiraffers.warehousemanagement.wms.storage.model;

/**
 * 입고 상태를 나타내는 ENUM 클래스입니다.
 */
public enum StorageStatus {

    WAITING("입고대기"),
    COMPLETED("입고완료"),
    DEFECTIVE("입고이상"),
    REJECTED("입고거절"),
    DELAYED("입고지연");

    private final String description;

    // 생성자
    StorageStatus(String description) {
        this.description = description;
    }

    // 상태 설명 반환
    public String getDescription() {
        return description;
    }
}
