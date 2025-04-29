package com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response;

import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StorageResponseDTO {

    private Integer storageId;             // 입고 ID (자동 생성)
    private Integer purchaseId;            // 발주 ID (직접 입력)
    private StorageStatus storageStatus;   // 입고 상태 (선택)
    private String inspectionStatus;       // 검수 상태 (입고 이상/완료 등)
    private LocalDate storageDate;         // 입고일 (입력)
    private String storageReason;          // 입고 사유 (입력)
    private LocalDateTime storageCreatedAt; // 등록일 (자동 생성)

    public StorageResponseDTO() {
    }

    public StorageResponseDTO(Integer storageId, Integer purchaseId, StorageStatus storageStatus, String inspectionStatus, LocalDate storageDate, String storageReason, LocalDateTime storageCreatedAt) {
        this.storageId = storageId;
        this.purchaseId = purchaseId;
        this.storageStatus = storageStatus;
        this.inspectionStatus = inspectionStatus;
        this.storageDate = storageDate;
        this.storageReason = storageReason;
        this.storageCreatedAt = storageCreatedAt;
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

    public String getStorageStatusDescription() {
        return storageStatus != null ? storageStatus.getDescription() : "";
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
                '}';
    }
}
