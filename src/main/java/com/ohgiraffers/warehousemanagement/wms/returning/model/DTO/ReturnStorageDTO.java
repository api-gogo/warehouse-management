package com.ohgiraffers.warehousemanagement.wms.returning.model.DTO;

import java.time.LocalDateTime;

public class ReturnStorageDTO {

    private Integer returnStorageId; //long?
    private Integer userId;

    private String returnStorageContent;
    private LocalDateTime returnStorageCreatedAt;

    private Integer storageId;

    public ReturnStorageDTO() {
    }

    //매개변수가 있는 생성자
    //조회 및 수정용 - PK값이 있는 생성자
    public ReturnStorageDTO(Integer returnStorageId, Integer userId, String returnStorageContent, LocalDateTime returnStorageCreatedAt, Integer storageId) {
        this.returnStorageId = returnStorageId;
        this.userId = userId;
        this.returnStorageContent = returnStorageContent;
        this.returnStorageCreatedAt = returnStorageCreatedAt;
        this.storageId = storageId;
    }
    //등록용 - PK값이 없는 생성자
    public ReturnStorageDTO(Integer userId, String returnStorageContent, LocalDateTime returnStorageCreatedAt, Integer storageId) {
        this.userId = userId;
        this.returnStorageContent = returnStorageContent;
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

    public String getReturnStorageContent() {
        return returnStorageContent;
    }

    public void setReturnStorageContent(String returnStorageContent) {
        this.returnStorageContent = returnStorageContent;
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
}
