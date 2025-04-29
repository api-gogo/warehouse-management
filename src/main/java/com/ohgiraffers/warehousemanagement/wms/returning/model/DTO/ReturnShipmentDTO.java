package com.ohgiraffers.warehousemanagement.wms.returning.model.DTO;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipments;

import java.time.LocalDateTime;

public class ReturnShipmentDTO {

    private Integer returnShipmentId;
    private Integer storeId;
    private Integer userId;

    private String lotNumber;
    private int returnShipmentQuantity;
    private String returnShipmentContent;
    private String returnShipmentStatus;

    private LocalDateTime returnShipmentCreatedAt;
    private LocalDateTime returnShipmentUpdatedAt;
    private LocalDateTime returnShipmentDeletedAt;

    private boolean isDeleted;

    private Integer shipmentId;


    public ReturnShipmentDTO() {
    }

    //매개변수가 있는 생성자

    //조회 및 수정용 - PK값 있음
    public ReturnShipmentDTO(Integer returnShipmentId, Integer storeId, Integer userId, String lotNumber, int returnShipmentQuantity, String returnShipmentContent, String returnShipmentStatus, LocalDateTime returnShipmentCreatedAt, LocalDateTime returnShipmentUpdatedAt, LocalDateTime returnShipmentDeletedAt, boolean isDeleted , Integer shipmentId) {
        this.returnShipmentId = returnShipmentId;//PK
        this.storeId = storeId;
        this.userId = userId;
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
    //등록 - PK 값 없음

    public ReturnShipmentDTO(Integer storeId, Integer userId, String lotNumber, int returnShipmentQuantity, String returnShipmentContent, String returnShipmentStatus, LocalDateTime returnShipmentCreatedAt, LocalDateTime returnShipmentUpdatedAt, LocalDateTime returnShipmentDeletedAt, boolean isDeleted, Integer shipmentId) {
        this.storeId = storeId;
        this.userId = userId;
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


    //게터세터

    public Integer getReturnShipmentId() {
        return returnShipmentId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public int getReturnShipmentQuantity() {
        return returnShipmentQuantity;
    }

    public String getReturnShipmentContent() {
        return returnShipmentContent;
    }

    public String getReturnShipmentStatus() {
        return returnShipmentStatus;
    }

    public LocalDateTime getReturnShipmentCreatedAt() {
        return returnShipmentCreatedAt;
    }

    public LocalDateTime getReturnShipmentUpdatedAt() {
        return returnShipmentUpdatedAt;
    }

    public LocalDateTime getReturnShipmentDeletedAt() {
        return returnShipmentDeletedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setReturnShipmentId(Integer returnShipmentId) {
        this.returnShipmentId = returnShipmentId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public void setReturnShipmentQuantity(int returnShipmentQuantity) {
        this.returnShipmentQuantity = returnShipmentQuantity;
    }

    public void setReturnShipmentContent(String returnShipmentContent) {
        this.returnShipmentContent = returnShipmentContent;
    }

    public void setReturnShipmentStatus(String returnShipmentStatus) {
        this.returnShipmentStatus = returnShipmentStatus;
    }

    public void setReturnShipmentCreatedAt(LocalDateTime returnShipmentCreatedAt) {
        this.returnShipmentCreatedAt = returnShipmentCreatedAt;
    }

    public void setReturnShipmentUpdatedAt(LocalDateTime returnShipmentUpdatedAt) {
        this.returnShipmentUpdatedAt = returnShipmentUpdatedAt;
    }

    public void setReturnShipmentDeletedAt(LocalDateTime returnShipmentDeletedAt) {
        this.returnShipmentDeletedAt = returnShipmentDeletedAt;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }
}
