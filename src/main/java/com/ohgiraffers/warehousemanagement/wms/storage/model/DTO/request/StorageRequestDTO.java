package com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.request;

import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class StorageRequestDTO {

    @NotNull(message = "발주 ID는 필수입니다!")
    private Integer purchaseId;  // 발주 ID

    @NotNull(message = "입고 상태를 선택해 주세요!")
    private StorageStatus storageStatus;  // 입고 상태 (ENUM)

    private String inspectionStatus;  // 검수 상태 (입고 이상/완료 등)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "입고일은 필수입니다!")
    private LocalDate storageDate;  // 입고일

    @NotBlank(message = "입고 사유를 입력해 주세요.")
    private String storageReason;  // 입고 사유

    // 등록일은 자동 생성 - 사용자가 입력하지 않음
    private LocalDate createdAt;

    public StorageRequestDTO() {
        this.createdAt = LocalDate.now();  // 객체 생성 시 등록일 자동 설정
    }

    public StorageRequestDTO(Integer purchaseId, StorageStatus storageStatus, String inspectionStatus, LocalDate storageDate, String storageReason, LocalDate createdAt) {
        this.purchaseId = purchaseId;
        this.storageStatus = storageStatus;
        this.inspectionStatus = inspectionStatus;
        this.storageDate = storageDate;
        this.storageReason = storageReason;
        this.createdAt = createdAt;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StorageRequestDTO{" +
                "purchaseId=" + purchaseId +
                ", storageStatus=" + storageStatus +
                ", inspectionStatus='" + inspectionStatus + '\'' +
                ", storageDate=" + storageDate +
                ", storageReason='" + storageReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
