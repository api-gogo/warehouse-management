package com.ohgiraffers.warehousemanagement.wms.purchases.model.dto;


import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDTO {
    private Integer purchaseId;
    private Long userId;
    private LocalDate purchaseDate;
    private LocalDate purchaseDueDate;
    private String purchaseStatus;
    private LocalDate purchaseUpdatedAt;
    private LocalDate purchaseCreatedAt;
    private String purchaseNotes;
    private Product product;


    private List<PurchaseItemDTO> items = new ArrayList<>();


//    private Supplier supplier;
//    private List<PurchaseItem> items = new ArrayList<>();
//    private Storages storages;


    public PurchaseDTO() {}


    public PurchaseDTO(Integer purchaseId, Long userId, LocalDate purchaseCreatedAt , LocalDate purchaseDate, LocalDate purchaseDueDate, String purchaseStatus, LocalDate purchaseUpdatedAt, String purchaseNotes
                       )
    {  //Supplier supplier, List<PurchaseItem> items,Storages storages
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.purchaseDueDate = purchaseDueDate;
        this.purchaseStatus = purchaseStatus;
        this.purchaseUpdatedAt = purchaseUpdatedAt;
        this.purchaseCreatedAt = purchaseCreatedAt;
        this.purchaseNotes = purchaseNotes;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public void setPurchaseUpdatedAt(LocalDate purchaseUpdatedAt) {
        this.purchaseUpdatedAt = purchaseUpdatedAt;
    }

    public LocalDate getPurchaseCreatedAt() {
        return purchaseCreatedAt;
    }

    public void setPurchaseCreatedAt(LocalDate purchaseCreatedAt) {
        this.purchaseCreatedAt = purchaseCreatedAt;
    }

    public String getPurchaseNotes() {
        return purchaseNotes;
    }

    public void setPurchaseNotes(String purchaseNotes) {
        this.purchaseNotes = purchaseNotes;
    }

    public List<PurchaseItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemDTO> items) {
        this.items = items;
    }

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
                ", purchase_notes='" + purchaseNotes + '\'' +
//                ", supplier=" + supplier +
                ", items=" + items +
//                ", storages=" + storages +
                '}';
    }
}
