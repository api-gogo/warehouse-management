package com.ohgiraffers.warehousemanagement.wms.storage.model.entity;

import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id")
    private Integer storageId;

    @Column(name = "purchase_id")
    private Integer purchaseId;

    @Column(name = "user_id")
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_status")
    private StorageStatus storageStatus;

    @Column(name = "inspection_status")  // 검수 상태 필드 추가
    private String inspectionStatus;  // 검수 상태 (예: 이상, 완료 등)

    @Column(name = "storage_date")
    private LocalDate storageDate;

    @Column(name = "storage_reason")
    private String storageReason;

    @Column(name = "storage_created_at", updatable = false)
    private LocalDateTime storageCreatedAt;

    @Column(name = "storage_updated_at")
    private LocalDateTime storageUpdatedAt;

    public Storage(Integer storageId, Integer purchaseId, Integer userId, StorageStatus storageStatus, String inspectionStatus, LocalDate storageDate, String storageReason, LocalDateTime storageCreatedAt, LocalDateTime storageUpdatedAt) {
        this.storageId = storageId;
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.storageStatus = storageStatus;
        this.inspectionStatus = inspectionStatus;
        this.storageDate = storageDate;
        this.storageReason = storageReason;
        this.storageCreatedAt = storageCreatedAt;
        this.storageUpdatedAt = storageUpdatedAt;
    }

    public Storage() {
    }

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public StorageStatus getStorageStatus() {
        return storageStatus;
    }

    public void setStorageStatus(StorageStatus storageStatus) {
        this.storageStatus = storageStatus;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public LocalDate getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(LocalDate storageDate) {
        this.storageDate = storageDate;
    }

    public String getStorageReason() {
        return storageReason;
    }

    public void setStorageReason(String storageReason) {
        this.storageReason = storageReason;
    }

    public LocalDateTime getStorageCreatedAt() {
        return storageCreatedAt;
    }

    public void setStorageCreatedAt(LocalDateTime storageCreatedAt) {
        this.storageCreatedAt = storageCreatedAt;
    }

    public LocalDateTime getStorageUpdatedAt() {
        return storageUpdatedAt;
    }

    public void setStorageUpdatedAt(LocalDateTime storageUpdatedAt) {
        this.storageUpdatedAt = storageUpdatedAt;
    }

    @Override
    public String toString() {
        return "입고{" +
                "입고 Id : " + storageId +
                ", 발주Id : " + purchaseId +
                ", 사용자 Id : " + userId +
                ", 입고 상태 : " + storageStatus +
                ", 검수 상태 : '" + inspectionStatus + '\'' +
                ", 입고 날짜 : =" + storageDate +
                ", 입고 이유 : ='" + storageReason + '\'' +
                ", 입고 등록일 : " + storageCreatedAt +
                ", 입고 수정일 : " + storageUpdatedAt +
                '}';
    }
}
