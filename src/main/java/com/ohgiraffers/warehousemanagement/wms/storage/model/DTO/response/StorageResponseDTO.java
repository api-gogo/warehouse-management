package com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response;

import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StorageResponseDTO {

    private Integer storageId;
    private Integer purchaseId;

    private StorageStatus storageStatus;
    private String inspectionStatus;

    private LocalDate storageDate;
    private String storageReason;
    private LocalDateTime storageCreatedAt;

    // 연관된 발주 정보
    private Long purchaseUserId;
    private String purchaseUserName;
    private LocalDate purchaseDueDate;
    private String purchaseStatus;

    private String supplierName;
    private int itemCount;

    public StorageResponseDTO() {}

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

    public Long getPurchaseUserId() {
        return purchaseUserId;
    }

    public void setPurchaseUserId(Long purchaseUserId) {
        this.purchaseUserId = purchaseUserId;
    }

    public String getPurchaseUserName() {
        return purchaseUserName;
    }

    public void setPurchaseUserName(String purchaseUserName) {
        this.purchaseUserName = purchaseUserName;
    }

    public LocalDate getPurchaseDueDate() {
        return purchaseDueDate;
    }

    public void setPurchaseDueDate(LocalDate purchaseDueDate) {
        this.purchaseDueDate = purchaseDueDate;
    }

    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    // 상태에 따른 한글 설명 반환
    public String getStorageStatusDescription() {
        if (storageStatus == null) return "";
        switch (storageStatus) {
            case WAITING: return "입고대기";
            case COMPLETED: return "입고완료";
            case DELAYED: return "입고지연";
            case DEFECTIVE: return "입고이상";
            case REJECTED: return "입고거절";
            default: return "알 수 없음";
        }
    }

    @Override
    public String toString() {
        return "StorageResponseDTO{" +
                "storageId=" + storageId +
                ", purchaseId=" + purchaseId +
                ", storageStatus=" + storageStatus +
                ", inspectionStatus='" + inspectionStatus + '\'' +
                ", storageDate=" + storageDate +
                ", storageReason='" + storageReason + '\'' +
                ", storageCreatedAt=" + storageCreatedAt +
                ", purchaseUserId=" + purchaseUserId +
                ", purchaseUserName='" + purchaseUserName + '\'' +
                ", purchaseDueDate=" + purchaseDueDate +
                ", purchaseStatus='" + purchaseStatus + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", itemCount=" + itemCount +
                '}';
    }
}
