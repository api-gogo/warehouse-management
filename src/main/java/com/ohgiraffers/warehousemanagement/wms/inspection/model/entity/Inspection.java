package com.ohgiraffers.warehousemanagement.wms.inspection.model.entity;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionStatus;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "inspections")
public class Inspection {

    @Id
    @Column(name = "inspection_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inspectionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @Comment("검수자 정보")
    private User user;

    @Column(name = "transaction_id")
    @Comment("검수의 출처 id값[null이면 자체검수]")
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    @Comment("검수 유형")
    private InspectionTransactionType transactionType;

    @Column(name = "inspection_quantity", nullable = false)
    @Comment("검수 수량")
    private int inspectionQuantity;

    @Column(name = "accepted_quantity", nullable = false)
    @Comment("정상 수량")
    private int acceptedQuantity;

    @Column(name = "defective_quantity", nullable = false)
    @Comment("불량 수량")
    private int defectiveQuantity;

    @Enumerated(EnumType.STRING)
    @Comment("검수 결과")
    private InspectionStatus inspectionStatus;

    @Column(name = "inspection_date", nullable = false, columnDefinition = "DATE")
    @Comment("검수 날짜")
    private LocalDate inspectionDate;

    @Column(name = "inspection_updated_at", columnDefinition = "TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime inspectionUpdatedAt;

    protected Inspection() {}

    public Inspection(User user, Long transactionId, InspectionTransactionType transactionType,
                      Integer inspectionQuantity, Integer acceptedQuantity, Integer defectiveQuantity,
                      InspectionStatus inspectionStatus, LocalDate inspectionDate) {
        this.user = user;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.inspectionQuantity = inspectionQuantity;
        this.acceptedQuantity = acceptedQuantity;
        this.defectiveQuantity = defectiveQuantity;
        this.inspectionStatus = inspectionStatus;
        this.inspectionDate = inspectionDate;
    }

    public Inspection(InspectionRequestDTO dto) {
        this.transactionId = dto.getTransactionId();
        this.transactionType = dto.getTransactionType();
        this.inspectionQuantity = dto.getInspectionQuantity();
        this.acceptedQuantity = dto.getAcceptedQuantity();
        this.defectiveQuantity = dto.getDefectiveQuantity();
        this.inspectionStatus = dto.getInspectionStatus();
        this.inspectionDate = dto.getInspectionDate();
    }



    public Long getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDateTime getInspectionUpdatedAt() {
        return inspectionUpdatedAt;
    }

    public void setInspectionUpdatedAt(LocalDateTime inspectionUpdatedAt) {
        this.inspectionUpdatedAt = inspectionUpdatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inspection that = (Inspection) o;
        return inspectionQuantity == that.inspectionQuantity && acceptedQuantity == that.acceptedQuantity && defectiveQuantity == that.defectiveQuantity && Objects.equals(inspectionId, that.inspectionId) && Objects.equals(user, that.user) && Objects.equals(transactionId, that.transactionId) && transactionType == that.transactionType && inspectionStatus == that.inspectionStatus && Objects.equals(inspectionDate, that.inspectionDate) && Objects.equals(inspectionUpdatedAt, that.inspectionUpdatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inspectionId, user, transactionId, transactionType, inspectionQuantity, acceptedQuantity, defectiveQuantity, inspectionStatus, inspectionDate, inspectionUpdatedAt);
    }

    @Override
    public String toString() {
        return "검수 : " +
                "검수 ID : " + inspectionId +
                ", 검수자 : " + user +
                (transactionId != null ? ", 검수 출처 ID : " + transactionId : "") +
                ", 검수 유형 : " + transactionType.getTransactionType() +
                ", 검수 수량 : " + inspectionQuantity +
                ", 정상 수량 : " + acceptedQuantity +
                ", 불량 수량 : " + defectiveQuantity +
                ", 검수 상태 : " + inspectionStatus.getInspectionStatus() +
                ", 검수 날짜 : " + inspectionDate +
                (inspectionUpdatedAt != null ? ", 수정 시간 : " + inspectionUpdatedAt : "") +
                "\n";
    }
}
