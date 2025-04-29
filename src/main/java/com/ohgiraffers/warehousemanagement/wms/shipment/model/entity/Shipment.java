package com.ohgiraffers.warehousemanagement.wms.shipment.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment {
    //출고번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id", nullable = false)
    private Integer shipmentId;

    //수주 ID    외래키이나 연결 전 이라 임시 컬럼
    @Column(name = "sale_id", nullable = false)
    private Integer saleId;
//    @OneToMany
//    @JoinColumn(name = "sale_id", nullable = false)

    //출고 담당자
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    //상품ID
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    //출고일
    @Column(name = "shipment_date", nullable = false)
    private LocalDateTime shipmentDate;
    //상태
    @Column(name = "shipment_status", nullable = false, length = 20)
    private String shipmentStatus;
    //사유
    @Column(name = "shipment_reason")
    private String shipmentReason;
    //수정일
    @Column(name = "shipment_updated_at")
    private LocalDateTime shipmentUpdatedAt;
    // 생성일
    @CreationTimestamp
    @Column(name = "shipment_created_at", nullable = false)
    private LocalDateTime shipmentCreatedAt;



    //생성자

    public Shipment() {}

    public Shipment(Integer shipmentId, Integer saleId, Integer userId, Integer productId, LocalDateTime shipmentDate, String shipmentStatus, String shipmentReason, LocalDateTime shipmentUpdatedAt, LocalDateTime shipmentCreatedAt) {
        this.shipmentId = shipmentId;
        this.saleId = saleId;
        this.userId = userId;
        this.productId = productId;
        this.shipmentDate = shipmentDate;
        this.shipmentStatus = shipmentStatus;
        this.shipmentReason = shipmentReason;
        this.shipmentUpdatedAt = shipmentUpdatedAt;
        this.shipmentCreatedAt = shipmentCreatedAt;
    }


    //gatter setter
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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


    //

    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentId=" + shipmentId +
                ", saleId=" + saleId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", shipmentDate=" + shipmentDate +
                ", shipmentStatus='" + shipmentStatus + '\'' +
                ", shipmentReason='" + shipmentReason + '\'' +
                ", shipmentUpdatedAt=" + shipmentUpdatedAt +
                ", shipmentCreatedAt=" + shipmentCreatedAt +
                '}';
    }
}
