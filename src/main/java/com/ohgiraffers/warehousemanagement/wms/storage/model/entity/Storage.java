package com.ohgiraffers.warehousemanagement.wms.storage.model.entity;

import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.Purchase;
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

    //  1:1 관계 - Purchase (연관관계 주인)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", referencedColumnName = "purchase_id", nullable = false)
    private Purchase purchase;

    // (입고 등록자 ID - Optional)
    @Column(name = "user_id")
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_status")
    private StorageStatus storageStatus;

    @Column(name = "inspection_status")
    private String inspectionStatus;

    @Column(name = "storage_date")
    private LocalDate storageDate;

    @Column(name = "storage_reason")
    private String storageReason;

    @Column(name = "storage_created_at", updatable = false)
    private LocalDateTime storageCreatedAt;

    @Column(name = "storage_updated_at")
    private LocalDateTime storageUpdatedAt;

    // 생성자
    public Storage() {}

    public Storage(Integer storageId, Purchase purchase, Integer userId, StorageStatus storageStatus,
                   String inspectionStatus, LocalDate storageDate, String storageReason,
                   LocalDateTime storageCreatedAt, LocalDateTime storageUpdatedAt) {
        this.storageId = storageId;
        this.purchase = purchase;
        this.userId = userId;
        this.storageStatus = storageStatus;
        this.inspectionStatus = inspectionStatus;
        this.storageDate = storageDate;
        this.storageReason = storageReason;
        this.storageCreatedAt = storageCreatedAt;
        this.storageUpdatedAt = storageUpdatedAt;
    }

    // Getters & Setters
    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
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
        return "Storage{" +
                "storageId=" + storageId +
                ", purchaseId=" + (purchase != null ? purchase.getPurchaseId() : "null") +
                ", userId=" + userId +
                ", storageStatus=" + storageStatus +
                ", inspectionStatus='" + inspectionStatus + '\'' +
                ", storageDate=" + storageDate +
                ", storageReason='" + storageReason + '\'' +
                ", storageCreatedAt=" + storageCreatedAt +
                ", storageUpdatedAt=" + storageUpdatedAt +
                '}';
    }
}
