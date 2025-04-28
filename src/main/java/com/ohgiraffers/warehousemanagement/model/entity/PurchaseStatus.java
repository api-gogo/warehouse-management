package com.ohgiraffers.warehousemanagement.model.entity;

public enum PurchaseStatus {
    대기("발주대기"),
    완료("발주완료"),
    거절("발주거절"),
    취소("발주취소");

    private final String label;
    // labal 을 따로 관리해서 PurchaseStatus.대기를 입력하면  "발주대기" 이런식으로 표시가 되게 관리함
    //getPurchaseStatus().getLabel() 를 입력해도  "발주대기"가 사용자에게 출력됨

    PurchaseStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }


}
