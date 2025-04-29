package com.ohgiraffers.warehousemanagement.wms.inspection.model.common;

public enum InspectionTransactionType {
    INSPECTION("상시",0),
    PURCHASE("발주", 1), //입고 검수
    SALES("수주",2), // 출고 검수
    STORAGE("입고반품",3),
    SHIPMENT("출고반품",4);

    private String transactionType; // 타입 설명
    private Integer typeId; // 타입 번호

    InspectionTransactionType(String transactionType, Integer typeId) {
        this.transactionType = transactionType;
        this.typeId = typeId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Integer getTypeId() {
        return typeId;
    }

    @Override
    public String toString() {
        return transactionType;
    }

    /**
     * String 입력값이 ENUM 타입 안에 들어있는지 확인하는 메서드
     * @param input String 타입의 입력 값
     * @return ENUM 타입 안에 일치하는 값이 들어있다면 true, 없다면 false 리턴
     */
    public static boolean typeContains(String input) {
        try {
            InspectionTransactionType.valueOf(input.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
