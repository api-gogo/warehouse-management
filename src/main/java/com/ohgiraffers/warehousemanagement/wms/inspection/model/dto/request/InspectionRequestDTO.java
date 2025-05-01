package com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionStatus;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class InspectionRequestDTO {
    @NotNull(message = "검수자는 필수입니다!")
    private Integer userId;

    private Long transactionId = null;

    @NotNull(message = "검수 유형은 필수입니다!")
    @Enumerated(EnumType.STRING)
    private InspectionTransactionType transactionType;

    @NotNull(message = "검수 수량은 필수입니다!")
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다!")
    private Integer inspectionQuantity;

    @NotNull(message = "정상 수량은 필수입니다!")
    @Min(value = 0, message = "수량은 0개 이상이어야 합니다!")
    private Integer acceptedQuantity;

    @NotNull(message = "불량 수량은 필수입니다!")
    @Min(value = 0, message = "수량은 0개 이상이어야 합니다!")
    private Integer defectiveQuantity;

    @NotNull(message = "검수 상태는 필수입니다!")
    @Enumerated(EnumType.STRING)
    private InspectionStatus inspectionStatus;

    @NotNull(message = "검수 날짜는 필수입니다!")
    private LocalDate inspectionDate;

    protected InspectionRequestDTO() {}

    public InspectionRequestDTO(Integer userId, Long transactionId, InspectionTransactionType transactionType,
                                Integer inspectionQuantity, Integer acceptedQuantity, Integer defectiveQuantity,
                                InspectionStatus inspectionStatus, LocalDate inspectionDate) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.inspectionQuantity = inspectionQuantity;
        this.acceptedQuantity = acceptedQuantity;
        this.defectiveQuantity = defectiveQuantity;
        this.inspectionStatus = inspectionStatus;
        this.inspectionDate = inspectionDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InspectionRequestDTO that = (InspectionRequestDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(transactionId, that.transactionId) && transactionType == that.transactionType && Objects.equals(inspectionQuantity, that.inspectionQuantity) && Objects.equals(acceptedQuantity, that.acceptedQuantity) && Objects.equals(defectiveQuantity, that.defectiveQuantity) && inspectionStatus == that.inspectionStatus && Objects.equals(inspectionDate, that.inspectionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, transactionId, transactionType, inspectionQuantity, acceptedQuantity, defectiveQuantity, inspectionStatus, inspectionDate);
    }

    @Override
    public String toString() {
        return "InspectionRequestDTO{" +
                "userId=" + userId +
                ", transactionId=" + transactionId +
                ", transactionType=" + transactionType +
                ", inspectionQuantity=" + inspectionQuantity +
                ", acceptedQuantity=" + acceptedQuantity +
                ", defectiveQuantity=" + defectiveQuantity +
                ", inspectionStatus=" + inspectionStatus +
                ", inspectionDate=" + inspectionDate +
                '}';
    }
}
