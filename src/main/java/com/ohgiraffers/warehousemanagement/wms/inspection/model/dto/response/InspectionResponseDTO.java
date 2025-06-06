package com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionStatus;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InspectionResponseDTO {
    private Long inspectionId;
    private Long userId;
    private String userName;
    private Long transactionId = null;
    private String transactionType;
    private Integer inspectionQuantity;
    private Integer acceptedQuantity;
    private Integer defectiveQuantity;
    private String inspectionStatus;
    private LocalDate inspectionDate;
    private LocalDateTime inspectionUpdatedAt;

    protected InspectionResponseDTO() {}

    public InspectionResponseDTO(Long inspectionId, Long userId, String userName, Long transactionId,
                                 InspectionTransactionType transactionType, Integer inspectionQuantity,
                                 Integer acceptedQuantity, Integer defectiveQuantity, InspectionStatus inspectionStatus,
                                 LocalDate inspectionDate, LocalDateTime inspectionUpdatedAt) {
        this.inspectionId = inspectionId;
        this.userId = userId;
        this.userName = userName;
        this.transactionId = transactionId;
        this.transactionType = transactionType.getTransactionType();
        this.inspectionQuantity = inspectionQuantity;
        this.acceptedQuantity = acceptedQuantity;
        this.defectiveQuantity = defectiveQuantity;
        this.inspectionStatus = inspectionStatus.getInspectionStatus();
        this.inspectionDate = inspectionDate;
        this.inspectionUpdatedAt = inspectionUpdatedAt;
    }

    public InspectionResponseDTO(Inspection saveInspection) {
        this.inspectionId = saveInspection.getInspectionId();
        this.userId = saveInspection.getUser().getUserId();
        this.userName = saveInspection.getUser().getUserName();
        this.transactionId = saveInspection.getTransactionId();
        this.transactionType = saveInspection.getTransactionType().getTransactionType();
        this.inspectionQuantity = saveInspection.getInspectionQuantity();
        this.acceptedQuantity = saveInspection.getAcceptedQuantity();
        this.defectiveQuantity = saveInspection.getDefectiveQuantity();
        this.inspectionStatus = saveInspection.getInspectionStatus().getInspectionStatus();
        this.inspectionDate = saveInspection.getInspectionDate();
        this.inspectionUpdatedAt = saveInspection.getInspectionUpdatedAt();
    }

    public Long getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(InspectionTransactionType transactionType) {
        this.transactionType = transactionType.getTransactionType();
    }

    public Integer getInspectionQuantity() {
        return inspectionQuantity;
    }

    public void setInspectionQuantity(Integer inspectionQuantity) {
        this.inspectionQuantity = inspectionQuantity;
    }

    public Integer getAcceptedQuantity() {
        return acceptedQuantity;
    }

    public void setAcceptedQuantity(Integer acceptedQuantity) {
        this.acceptedQuantity = acceptedQuantity;
    }

    public Integer getDefectiveQuantity() {
        return defectiveQuantity;
    }

    public void setDefectiveQuantity(Integer defectiveQuantity) {
        this.defectiveQuantity = defectiveQuantity;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(InspectionStatus inspectionStatus) {
        this.inspectionStatus = inspectionStatus.getInspectionStatus();
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public LocalDateTime getInspectionUpdatedAt() {
        return inspectionUpdatedAt;
    }

    public void setInspectionUpdatedAt(LocalDateTime inspectionUpdatedAt) {
        this.inspectionUpdatedAt = inspectionUpdatedAt;
    }

    @Override
    public String toString() {
        return "InspectionResponseDTO{" +
                "inspectionId=" + inspectionId +
                ", userId=" + userId +
                ", userName='" + userName +
                ", transactionId=" + transactionId +
                ", transactionType=" + transactionType +
                ", inspectionQuantity=" + inspectionQuantity +
                ", acceptedQuantity=" + acceptedQuantity +
                ", defectiveQuantity=" + defectiveQuantity +
                ", inspectionStatus=" + inspectionStatus +
                ", inspectionDate=" + inspectionDate +
                ", inspectionUpdatedAt=" + inspectionUpdatedAt +
                '}';
    }
}
