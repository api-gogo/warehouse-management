package com.ohgiraffers.warehousemanagement.wms.user.model.common;

public enum UserPart {
    통합("통합"),
    입고("입고"),
    출고("출고"),
    재고("재고"),
    검수("검수"),
    반품("반품"),
    발주("발주"),
    수주("수주"),
    상품("상품");

    private String part;

    UserPart(String part) {
        this.part = part;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
