package com.ohgiraffers.warehousemanagement.wms.returning.model.entity;

import com.sun.jdi.LongValue;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "return_storages")

public class ReturnStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_storage_id",nullable = false)
    private Integer returnStorageId; //-> PK값

    @Column(name = "storage_id",nullable = false)
    private Integer storageId; //-> 나중에 입고에서 외래키로 받아줘야됨

    @Column(name = "user_id",nullable = false)
    private Integer userId;

    @Column(name = "return_storages_content",nullable = false)
    private String returnStoragesContent;

    @Column(name = "return_storages_created_at",nullable = false)
    private LocalDateTime returnStoragesCreatedAt;

    /*생성자*/
    public ReturnStorage() {
    }
    /*조회 및 수정용 PK값 있음*/
    public ReturnStorage(Integer returnStorageId, Integer storageId, Integer userId, String returnStoragesContent, LocalDateTime returnStoragesCreatedAt) {
        this.returnStorageId = returnStorageId;
        this.storageId = storageId;
        this.userId = userId;
        this.returnStoragesContent = returnStoragesContent;
        this.returnStoragesCreatedAt = returnStoragesCreatedAt;
    }
    /* 등록용 - PK값 없음 */
    public ReturnStorage(Integer storageId, Integer userId, LocalDateTime returnStoragesCreatedAt, String returnStoragesContent) {
        this.storageId = storageId;
        this.userId = userId;
        this.returnStoragesCreatedAt = returnStoragesCreatedAt;
        this.returnStoragesContent = returnStoragesContent;
    }

    /*게터세터*/
    public Integer getReturnStorageId() {
        return returnStorageId;
    }

    public void setReturnStorageId(Integer returnStorageId) {
        this.returnStorageId = returnStorageId;
    }

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReturnStoragesContent() {
        return returnStoragesContent;
    }

    public void setReturnStoragesContent(String returnStoragesContent) {
        this.returnStoragesContent = returnStoragesContent;
    }

    public LocalDateTime getReturnStoragesCreatedAt() {
        return returnStoragesCreatedAt;
    }

    public void setReturnStoragesCreatedAt(LocalDateTime returnStoragesCreatedAt) {
        this.returnStoragesCreatedAt = returnStoragesCreatedAt;
    }
}
