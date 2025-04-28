package com.ohgiraffers.warehousemanagement.wms.returning.model.entity;

import com.ohgiraffers.warehousemanagement.wms.returning.model.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipments;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "return_shipments")
@Where(clause = "is_deleted = true") //삭제 상태가 1인 것만 조회

public class ReturnShipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="return_shipment_id",nullable = false)
    private Integer returnShipmentId; //PK값

    @OneToOne(fetch = FetchType.EAGER) //FK값
    @JoinColumn(name = "shipment_id",nullable = false)
    private Shipments shipmentId; //shipment클래스의 shipment_id를 외래키로 받겠다

    @Column(name = "user_id",nullable = false)
    private Integer userId;
    @Column(name = "store_id",nullable = false)
    private Integer storeId;

    @Column(name = "lot_number",nullable = false)
    private String lotNumber;
    @Column(name = "return_shipment_quantity",nullable = false)
    private int returnShipmentQuantity;
    @Column(name = "return_shipment_content",nullable = false)
    private String returnShipmentContent;
    @Column(name = "return_shipment_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private ReturnShipmentStatus returnShipmentStatus;

    @Column(name ="return_shipment_created_at",nullable = false)
    private LocalDateTime returnShipmentCreatedAt;
    @Column(name ="return_shipment_updated_at")
    private LocalDateTime returnShipmentUpdatedAt;
    @Column(name = "return_shipment_deleted_at")
    private LocalDateTime returnShipmentDeletedAt;

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted;


    public ReturnShipment() {
    }

    public ReturnShipment(Integer returnShipmentId, Integer userId, Integer storeId, String lotNumber, int returnShipmentQuantity, String returnShipmentContent, ReturnShipmentStatus returnShipmentStatus, LocalDateTime returnShipmentCreatedAt, LocalDateTime returnShipmentUpdatedAt, LocalDateTime returnShipmentDeletedAt, boolean isDeleted,Shipments shipmentId) {
        this.returnShipmentId = returnShipmentId;
        this.userId = userId;
        this.storeId = storeId;
        this.lotNumber = lotNumber;
        this.returnShipmentQuantity = returnShipmentQuantity;
        this.returnShipmentContent = returnShipmentContent;
        this.returnShipmentStatus = returnShipmentStatus;
        this.returnShipmentCreatedAt = returnShipmentCreatedAt;
        this.returnShipmentUpdatedAt = returnShipmentUpdatedAt;
        this.returnShipmentDeletedAt = returnShipmentDeletedAt;
        this.isDeleted = isDeleted;
        this.shipmentId = shipmentId;
    }

    public ReturnShipment(Integer storeId,Integer userId,String lotNumber,int returnShipmentQuantity,String returnShipmentContent,ReturnShipmentStatus returnShipmentStatus,LocalDateTime returnShipmentCreatedAt,Shipments shipmentId)
    {
        this.storeId = storeId;
        this.userId = userId;
        this.lotNumber = lotNumber;
        this.returnShipmentQuantity = returnShipmentQuantity;
        this.returnShipmentContent = returnShipmentContent;
        this.returnShipmentStatus = returnShipmentStatus;
        this.returnShipmentCreatedAt = returnShipmentCreatedAt;
        this.shipmentId = shipmentId;
    }

    public Integer getReturnShipmentId() {
        return returnShipmentId;
    }

    public void setReturnShipmentId(Integer returnShipmentId) {
        this.returnShipmentId = returnShipmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public int getReturnShipmentQuantity() {
        return returnShipmentQuantity;
    }

    public void setReturnShipmentQuantity(int returnShipmentQuantity) {
        this.returnShipmentQuantity = returnShipmentQuantity;
    }

    public String getReturnShipmentContent() {
        return returnShipmentContent;
    }

    public void setReturnShipmentContent(String returnShipmentContent) {
        this.returnShipmentContent = returnShipmentContent;
    }

    public ReturnShipmentStatus getReturnShipmentStatus() {
        return returnShipmentStatus;
    }

    public void setReturnShipmentStatus(ReturnShipmentStatus returnShipmentStatus) {
        this.returnShipmentStatus = returnShipmentStatus;
    }

    public LocalDateTime getReturnShipmentCreatedAt() {
        return returnShipmentCreatedAt;
    }

    public void setReturnShipmentCreatedAt(LocalDateTime returnShipmentCreatedAt) {
        this.returnShipmentCreatedAt = returnShipmentCreatedAt;
    }

    public LocalDateTime getReturnShipmentUpdatedAt() {
        return returnShipmentUpdatedAt;
    }

    public void setReturnShipmentUpdatedAt(LocalDateTime returnShipmentUpdatedAt) {
        this.returnShipmentUpdatedAt = returnShipmentUpdatedAt;
    }

    public LocalDateTime getReturnShipmentDeletedAt() {
        return returnShipmentDeletedAt;
    }

    public void setReturnShipmentDeletedAt(LocalDateTime returnShipmentDeletedAt) {
        this.returnShipmentDeletedAt = returnShipmentDeletedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Shipments getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Shipments shipmentId) {
        this.shipmentId = shipmentId;
    }

    @Override
    public String toString() {
        return "ReturnShipment{" +
                "returnShipmentId=" + returnShipmentId +
                ", shipmentId=" + shipmentId +
                ", userId=" + userId +
                ", storeId=" + storeId +
                ", lotNumber='" + lotNumber + '\'' +
                ", returnShipmentQuantity=" + returnShipmentQuantity +
                ", returnShipmentContent='" + returnShipmentContent + '\'' +
                ", returnShipmentStatus='" + returnShipmentStatus + '\'' +
                ", returnShipmentCreatedAt=" + returnShipmentCreatedAt +
                ", returnShipmentUpdatedAt=" + returnShipmentUpdatedAt +
                ", returnShipmentDeletedAt=" + returnShipmentDeletedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

