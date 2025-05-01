package com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response;

import java.time.LocalDate;

public class PurchaseInfoResponseDTO {

    private Integer purchaseId;
    private Long userId;
    private String userName;
    private LocalDate purchaseDueDate;
    private String purchaseStatus;
    private String inspectionStatus;
    private String supplierName;
    private int itemCount;

    public PurchaseInfoResponseDTO() {
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
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

    @Override
    public String toString() {
        return "PurchaseInfoResponseDTO{" +
                "purchaseId=" + purchaseId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", purchaseDueDate=" + purchaseDueDate +
                ", purchaseStatus='" + purchaseStatus + '\'' +
                ", inspectionStatus='" + inspectionStatus + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", itemCount=" + itemCount +
                '}';
    }
}
