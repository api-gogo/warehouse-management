package com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.request;

import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class StorageRequestDTO {

    @NotNull(message = "발주 ID는 필수입니다!")
    private Integer purchaseId;

    @NotNull(message = "입고 상태를 선택해 주세요!")
    private StorageStatus storageStatus;

    private String inspectionStatus;  // 검수 상태 (검수완료 / 검수이상)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "입고일은 필수입니다!")
    private LocalDate storageDate;

    @NotBlank(message = "입고 사유를 입력해 주세요.")
    private String storageReason;

    public StorageRequestDTO() {}

    public StorageRequestDTO(Integer purchaseId, StorageStatus storageStatus, String inspectionStatus,
                             LocalDate storageDate, String storageReason) {
        this.purchaseId = purchaseId;
        this.storageStatus = storageStatus;
        this.inspectionStatus = inspectionStatus;
        this.storageDate = storageDate;
        this.storageReason = storageReason;
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

    @Override
    public String toString() {
        return "StorageRequestDTO{" +
                "purchaseId=" + purchaseId +
                ", storageStatus=" + storageStatus +
                ", inspectionStatus='" + inspectionStatus + '\'' +
                ", storageDate=" + storageDate +
                ", storageReason='" + storageReason + '\'' +
                '}';
    }
}
