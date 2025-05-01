package com.ohgiraffers.warehousemanagement.wms.returning.model.DTO;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturningStorageCause;

import java.time.LocalDateTime;
import java.util.List;

public class ReturnStorageDTO {

    private Integer returnStorageId;
    private Integer userId;


    private LocalDateTime returnStorageCreatedAt;
    private LocalDateTime returnStorageUpdatedAt;
    private LocalDateTime returnStorageDeletedAt;

    private Integer storageId; //외래키는 타입만

    private List<ReturningStorageCause> returnStorageContent; //사유, 게터세터만

    private List<Integer> returnStorageQuantity; //수량


    public ReturnStorageDTO() {
    }

    //매개변수가 있는 생성자
    //조회 및 수정용 - PK값이 있는 생성자
    public ReturnStorageDTO(Integer returnStorageId, Integer userId, LocalDateTime returnStorageCreatedAt, Integer storageId) {
        this.returnStorageId = returnStorageId;
        this.userId = userId;
        this.returnStorageCreatedAt = returnStorageCreatedAt;
        this.storageId = storageId;
    }
    //등록용 - PK값이 없는 생성자
    public ReturnStorageDTO(Integer userId, LocalDateTime returnStorageCreatedAt, Integer storageId) {
        this.userId = userId;
        this.returnStorageCreatedAt = returnStorageCreatedAt;
        this.storageId = storageId;
    }

    //게터세터
    public Integer getReturnStorageId() {
        return returnStorageId;
    }

    public void setReturnStorageId(Integer returnStorageId) {
        this.returnStorageId = returnStorageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public LocalDateTime getReturnStorageCreatedAt() {
        return returnStorageCreatedAt;
    }

    public void setReturnStorageCreatedAt(LocalDateTime returnStorageCreatedAt) {
        this.returnStorageCreatedAt = returnStorageCreatedAt;
    }

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public List<ReturningStorageCause> getReturnStorageContent() {
        return returnStorageContent;
    }

    public void setReturnStorageContent(List<ReturningStorageCause> returnStorageContent) {
        this.returnStorageContent = returnStorageContent;
    }

    public List<Integer> getReturnStorageQuantity() {
        return returnStorageQuantity;
    }

    public void setReturnStorageQuantity(List<Integer> returnStorageQuantity) {
        this.returnStorageQuantity = returnStorageQuantity;
    }

    @Override
    public String toString() {
        return "ReturnStorageDTO{" +
                "returnStorageId=" + returnStorageId +
                ", userId=" + userId +
                ", returnStorageCreatedAt=" + returnStorageCreatedAt +
                ", storageId=" + storageId +
                ", returnStorageContent=" + returnStorageContent +
                ", returnShipmentQuantity=" + returnStorageQuantity +
                '}';
    }
}
