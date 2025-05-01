package com.ohgiraffers.warehousemanagement.wms.returning.model.DTO;


import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.ReturningShipmentCause;

import java.time.LocalDateTime;
import java.util.List;

public class ReturnShipmentDTO {

    private Integer returnShipmentId;
    private Integer storeId;
    private Integer userId;

    private ReturnShipmentStatus returnShipmentStatus; //Enum

    private LocalDateTime returnShipmentCreatedAt;
    private LocalDateTime returnShipmentUpdatedAt;
    private LocalDateTime returnShipmentDeletedAt;

    private boolean isDeleted;

    private Integer shipmentId; //FK값

    private List<String> lotNumber;
    private List<Integer> returnShipmentQuantity;
    private List<ReturningShipmentCause> returnShipmentContent; //나중에 확인 필요

    public ReturnShipmentDTO() {
    }

    //매개변수가 있는 생성자


    //조회 및 수정용 - PK값 있음
    public ReturnShipmentDTO(Integer returnShipmentId, Integer storeId, Integer userId, ReturnShipmentStatus returnShipmentStatus, LocalDateTime returnShipmentCreatedAt, LocalDateTime returnShipmentUpdatedAt, LocalDateTime returnShipmentDeletedAt, boolean isDeleted , Integer shipmentId) {
        this.returnShipmentId = returnShipmentId;//PK
        this.storeId = storeId;
        this.userId = userId;
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

    public void setReturnShipmentId(Integer returnShipmentId) {
        this.returnShipmentId = returnShipmentId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public List<String> getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(List<String> lotNumber) {
        this.lotNumber = lotNumber;
    }

    public List<Integer> getReturnShipmentQuantity() {
        return returnShipmentQuantity;
    }

    public void setReturnShipmentQuantity(List<Integer> returnShipmentQuantity) {
        this.returnShipmentQuantity = returnShipmentQuantity;
    }

    public List<ReturningShipmentCause> getReturnShipmentContent() {
        return returnShipmentContent;
    }

    public void setReturnShipmentContent(List<ReturningShipmentCause> returnShipmentContent) {
        this.returnShipmentContent = returnShipmentContent;
    }

    @Override
    public String toString() {
        return "ReturnShipmentDTO{" +
                "returnShipmentId=" + returnShipmentId +
                ", storeId=" + storeId +
                ", userId=" + userId +
                ", returnShipmentStatus='" + returnShipmentStatus + '\'' +
                ", returnShipmentCreatedAt=" + returnShipmentCreatedAt +
                ", returnShipmentUpdatedAt=" + returnShipmentUpdatedAt +
                ", returnShipmentDeletedAt=" + returnShipmentDeletedAt +
                ", isDeleted=" + isDeleted +
                ", shipmentId=" + shipmentId +
                ", lotNumber=" + lotNumber +
                ", returnShipmentQuantity=" + returnShipmentQuantity +
                ", returnShipmentContent=" + returnShipmentContent +
                '}';
    }
}
