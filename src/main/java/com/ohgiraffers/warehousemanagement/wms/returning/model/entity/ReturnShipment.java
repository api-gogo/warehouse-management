package com.ohgiraffers.warehousemanagement.wms.returning.model.entity;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "return_shipments")
//@Where(clause = "is_deleted = true") //활성화 상태인 것만 조회

public class ReturnShipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="return_shipment_id",nullable = false)
    private Integer returnShipmentId; //PK값

    /*@OneToOne(fetch = FetchType.EAGER) //FK값
    @JoinColumn(name = "shipment_id",nullable = false)
    private Shipment shipmentId; //shipment클래스의 shipment_id를 외래키로 받겠다*/

    @Column(name = "shipment_id",nullable = false)
    private Integer shipmentId; //나중에 외래키로, 출고와 외래키

    @Column(name = "user_id",nullable = false)
    private Long userId;
    @Column(name = "store_id",nullable = false)
    private Integer storeId;

    @Column(name = "lot_number"/*,nullable = false*/)
    private String lotNumber;

    @Column(name = "return_shipment_quantity",nullable = false)
    private int returnShipmentQuantity;


    @Enumerated(EnumType.STRING)
    @Column(name = "return_shipment_status",nullable = false)
    private ReturnShipmentStatus returnShipmentStatus;

    @Column(name ="return_shipment_created_at",nullable = false)
    private LocalDateTime returnShipmentCreatedAt;

    @Column(name ="return_shipment_updated_at")
    private LocalDateTime returnShipmentUpdatedAt;

    @Column(name = "return_shipment_deleted_at")
    private LocalDateTime returnShipmentDeletedAt;

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = true; // true=활성화, false=삭제

    @OneToMany(mappedBy = "returnShipmentId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ReturnShipmentItem> returnShipmentItems = new ArrayList<>(); //출고반품에 대해 리스트로 받아옴

    public ReturnShipment() {
    }

    public ReturnShipment(Integer returnShipmentId, Long userId, Integer storeId, String lotNumber, int returnShipmentQuantity, ReturnShipmentStatus returnShipmentStatus, LocalDateTime returnShipmentCreatedAt, LocalDateTime returnShipmentUpdatedAt, LocalDateTime returnShipmentDeletedAt, boolean isDeleted,Integer shipmentId) {
        this.returnShipmentId = returnShipmentId;//pk값
        this.userId = userId;
        this.storeId = storeId;
        this.lotNumber = lotNumber;
        this.returnShipmentQuantity = returnShipmentQuantity;
        this.returnShipmentStatus = returnShipmentStatus;
        this.returnShipmentCreatedAt = returnShipmentCreatedAt;
        this.returnShipmentUpdatedAt = returnShipmentUpdatedAt;
        this.returnShipmentDeletedAt = returnShipmentDeletedAt;
        this.isDeleted = isDeleted;
        this.shipmentId = shipmentId;
    }

    public ReturnShipment(Integer storeId,Long userId,String lotNumber,int returnShipmentQuantity,ReturnShipmentStatus returnShipmentStatus,LocalDateTime returnShipmentCreatedAt,Integer shipmentId)
    {
        this.storeId = storeId;
        this.userId = userId;
        this.lotNumber = lotNumber;
        this.returnShipmentQuantity = returnShipmentQuantity;
        this.returnShipmentStatus = returnShipmentStatus;
        this.returnShipmentCreatedAt = returnShipmentCreatedAt;
        this.shipmentId = shipmentId;
    }

    public List<ReturnShipmentItem> getReturnShipmentItems() {
        return returnShipmentItems;
    }

    public void setReturnShipmentItems(List<ReturnShipmentItem> returnShipmentItems) {
        this.returnShipmentItems = returnShipmentItems;
    }

    public Integer getReturnShipmentId() {
        return returnShipmentId;
    }

    public void setReturnShipmentId(Integer returnShipmentId) {
        this.returnShipmentId = returnShipmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
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
                ", returnShipmentStatus='" + returnShipmentStatus + '\'' +
                ", returnShipmentCreatedAt=" + returnShipmentCreatedAt +
                ", returnShipmentUpdatedAt=" + returnShipmentUpdatedAt +
                ", returnShipmentDeletedAt=" + returnShipmentDeletedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

