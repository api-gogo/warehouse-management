package com.ohgiraffers.warehousemanagement.wms.shipment.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment {

    // 출고번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id", nullable = false, columnDefinition = "BIGINT")
    private Integer shipmentId;

    // 수주 ID (외래키, 임시 컬럼)
    @NotNull(message = "수주 ID는 필수 입력 항목입니다.")
    @Column(name = "sale_id", nullable = false, columnDefinition = "BIGINT")
    private Integer saleId;

    // 출고 담당자
    @NotNull(message = "출고 담당자 ID는 필수 입력 항목입니다.")
    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT")
    private Integer userId;

    // 출고일
    @NotNull(message = "출고일은 필수 입력 항목입니다.")
    @Column(name = "shipment_date", nullable = false)
    private LocalDateTime shipmentDate;

    // 상태 (String으로 복구)
    @NotNull(message = "출고 상태는 필수 입력 항목입니다.")
    @Size(max = 20, message = "출고 상태는 20자를 초과할 수 없습니다.")
    @Column(name = "shipment_status", nullable = false, length = 20)
    private String shipmentStatus;

    // 사유
    @Column(name = "shipment_reason")
    private String shipmentReason;

    // 수정일
    @UpdateTimestamp
    @Column(name = "shipment_updated_at")
    private LocalDateTime shipmentUpdatedAt;

    // 등록일
    @CreationTimestamp
    @Column(name = "shipment_created_at", nullable = false)
    private LocalDateTime shipmentCreatedAt;

    // 생성자
    public Shipment() {}

    public Shipment(Integer shipmentId, Integer saleId, Integer userId, LocalDateTime shipmentDate, String shipmentStatus, String shipmentReason, LocalDateTime shipmentUpdatedAt, LocalDateTime shipmentCreatedAt) {
        this.shipmentId = shipmentId;
        this.saleId = saleId;
        this.userId = userId;
        this.shipmentDate = shipmentDate;
        this.shipmentStatus = shipmentStatus;
        this.shipmentReason = shipmentReason;
        this.shipmentUpdatedAt = shipmentUpdatedAt;
        this.shipmentCreatedAt = shipmentCreatedAt;
    }

    // 게터, 세터
    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDateTime shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getShipmentReason() {
        return shipmentReason;
    }

    public void setShipmentReason(String shipmentReason) {
        this.shipmentReason = shipmentReason;
    }

    public LocalDateTime getShipmentUpdatedAt() {
        return shipmentUpdatedAt;
    }

    public void setShipmentUpdatedAt(LocalDateTime shipmentUpdatedAt) {
        this.shipmentUpdatedAt = shipmentUpdatedAt;
    }

    public LocalDateTime getShipmentCreatedAt() {
        return shipmentCreatedAt;
    }

    public void setShipmentCreatedAt(LocalDateTime shipmentCreatedAt) {
        this.shipmentCreatedAt = shipmentCreatedAt;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentId=" + shipmentId +
                ", saleId=" + saleId +
                ", userId=" + userId +
                ", shipmentDate=" + shipmentDate +
                ", shipmentStatus='" + shipmentStatus + '\'' +
                ", shipmentReason='" + shipmentReason + '\'' +
                ", shipmentUpdatedAt=" + shipmentUpdatedAt +
                ", shipmentCreatedAt=" + shipmentCreatedAt +
                '}';
    }
}