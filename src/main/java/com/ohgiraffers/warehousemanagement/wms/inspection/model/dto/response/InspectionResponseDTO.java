package com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionStatus;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InspectionResponseDTO {
    private Integer inspectionId;
    private Integer userId;
    private Integer transactionId = null;
    private InspectionTransactionType transactionType;
    private Integer inspectionQuantity;
    private Integer acceptedQuantity;
    private Integer defectiveQuantity;
    private InspectionStatus inspectionStatus;
    private LocalDate inspectionDate;
    private LocalDateTime inspectionUpdatedAt;

    protected InspectionResponseDTO() {}

    public InspectionResponseDTO(Integer inspectionId, Integer userId, Integer transactionId,
                                 InspectionTransactionType transactionType, Integer inspectionQuantity,
                                 Integer acceptedQuantity, Integer defectiveQuantity, InspectionStatus inspectionStatus,
                                 LocalDate inspectionDate, LocalDateTime inspectionUpdatedAt) {
        this.inspectionId = inspectionId;
        this.userId = userId;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.inspectionQuantity = inspectionQuantity;
        this.acceptedQuantity = acceptedQuantity;
        this.defectiveQuantity = defectiveQuantity;
        this.inspectionStatus = inspectionStatus;
        this.inspectionDate = inspectionDate;
        this.inspectionUpdatedAt = inspectionUpdatedAt;
    }

    public InspectionResponseDTO(Inspection saveInspection) {
        this.inspectionId = saveInspection.getInspectionId();
        this.userId = saveInspection.getUserId();
        this.transactionId = saveInspection.getTransactionId();
        this.transactionType = saveInspection.getTransactionType();
        this.inspectionQuantity = saveInspection.getInspectionQuantity();
        this.acceptedQuantity = saveInspection.getAcceptedQuantity();
        this.defectiveQuantity = saveInspection.getDefectiveQuantity();
        this.inspectionStatus = saveInspection.getInspectionStatus();
        this.inspectionDate = saveInspection.getInspectionDate();
        this.inspectionUpdatedAt = saveInspection.getInspectionUpdatedAt();
    }

    public Integer getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(Integer inspectionId) {
        this.inspectionId = inspectionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public InspectionTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(InspectionTransactionType transactionType) {
        this.transactionType = transactionType;
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

    public InspectionStatus getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(InspectionStatus inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
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
