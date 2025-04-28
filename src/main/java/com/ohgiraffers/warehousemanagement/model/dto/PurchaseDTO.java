package com.ohgiraffers.warehousemanagement.model.dto;


import java.time.LocalDate;

public class PurchaseDTO {
    private Integer purchaseId;
    private Integer userId;
    private LocalDate purchaseDate;
    private LocalDate purchaseDueDate;
    private String purchaseStatus;
    private LocalDate purchaseUpdatedAt;
    private LocalDate purchaseCreatedAt;

//    private Supplier supplier;
//    private List<PurchaseItem> items = new ArrayList<>();
//    private Storages storages;


    public PurchaseDTO() {}


    public PurchaseDTO(Integer purchaseId, Integer userId,LocalDate purchaseCreatedAt ,LocalDate purchaseDate, LocalDate purchaseDueDate, String purchaseStatus, LocalDate purchaseUpdatedAt
                       )
    {  //Supplier supplier, List<PurchaseItem> items,Storages storages
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.purchaseDueDate = purchaseDueDate;
        this.purchaseStatus = purchaseStatus;
        this.purchaseUpdatedAt = purchaseUpdatedAt;
        this.purchaseCreatedAt = purchaseCreatedAt;
//        this.supplier = supplier;
//        this.items = items;
//        this.storages = storages;
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

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public LocalDate getPurchaseUpdatedAt() {
        return purchaseUpdatedAt;
    }

    public void setPurchaseUpdatedAt(LocalDate purchase_updated_at) {
        this.purchaseUpdatedAt = purchaseUpdatedAt;
    }

    public LocalDate getPurchaseCreatedAt() {
        return purchaseCreatedAt;
    }

    public void setPurchaseCreatedAt(LocalDate purchase_created_at) {
        this.purchaseCreatedAt = purchaseCreatedAt;
    }
    //    public Supplier getSupplier() {
//        return supplier;
//    }
//
//    public void setSupplier(Supplier supplier) {
//        this.supplier = supplier;
//    }
//
//    public List<PurchaseItem> getItems() {
//        return items;
//    }
//
//    public void setItems(List<PurchaseItem> items) {
//        this.items = items;
//    }
//
//    public Storages getStorages() {
//        return storages;
//    }
//
//    public void setStorages(Storages storages) {
//        this.storages = storages;
//    }

    @Override
    public String toString() {
        return "PurchaseDTO{" +
                "purchaseId=" + purchaseId +
                ", userId=" + userId +
                ", purchaseDate=" + purchaseDate +
                ", purchaseDueDate=" + purchaseDueDate +
                ", purchaseStatus='" + purchaseStatus + '\'' +
                ", purchase_updated_at=" + purchaseUpdatedAt +
                ", purchase_created_at=" + purchaseCreatedAt +
//                ", supplier=" + supplier +
//                ", items=" + items +
//                ", storages=" + storages +
                '}';
    }
}
