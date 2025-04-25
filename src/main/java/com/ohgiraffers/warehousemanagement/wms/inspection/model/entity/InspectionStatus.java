package com.ohgiraffers.warehousemanagement.wms.inspection.model.entity;

public enum InspectionStatus {
    OK("이상 없음",1),
    DEFECTIVE("이상 있음",2),
    HOLD("보류",3)
    ;


    private String inspectionStatus; // 스테이터스 설명
    private Integer statusId;

    InspectionStatus(String inspectionStatus, Integer statusId) {
        this.inspectionStatus = inspectionStatus;
        this.statusId = statusId;
    }

    // 스테이터스 번호

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public Integer getStatusId() {
        return statusId;
    }

    @Override
    public String toString() {
        return "검수상태{" +
                "inspectionStatus=" + inspectionStatus +
                ", statusId=" + statusId +
                '}';
    }
}
