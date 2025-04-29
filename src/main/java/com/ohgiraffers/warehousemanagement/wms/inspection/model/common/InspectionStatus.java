package com.ohgiraffers.warehousemanagement.wms.inspection.model.common;

public enum InspectionStatus {
    OK("이상 없음",1),
    DEFECTIVE("이상 있음",2),
    HOLD("보류",3)
    ;


    private String inspectionStatus; // 스테이터스 설명
    private Integer statusId; // 스테이터스 번호

    InspectionStatus(String inspectionStatus, Integer statusId) {
        this.inspectionStatus = inspectionStatus;
        this.statusId = statusId;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public Integer getStatusId() {
        return statusId;
    }

    @Override
    public String toString() {
        return inspectionStatus;
    }

    /**
     * String 타입의 inspectionStatus를 inspectionStatus로 반환하는 메서드
     * @param inspectionStatus String 형태의 inspectionStatus
     * @return 입력값을 inspectionStatus 타입으로 변환
     * @exception IllegalArgumentException 입력받은 String 값과 일치하는 항목이 inspectionStatus에 없을 때
     */
    public static InspectionStatus stringToInspectionStatus(String inspectionStatus) {
        if(inspectionStatus.equalsIgnoreCase("OK") || inspectionStatus.equals("이상 없음")){
            return OK;
        } else if(inspectionStatus.equalsIgnoreCase("DEFECTIVE") || inspectionStatus.equals("이상 있음")){
            return DEFECTIVE;
        } else if(inspectionStatus.equalsIgnoreCase("HOLD") || inspectionStatus.equals("보류")){
            return HOLD;
        } else {
            throw new IllegalArgumentException("일치하는 항목이 없습니다!");
        }
    }
}
