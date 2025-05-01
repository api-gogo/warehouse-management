package com.ohgiraffers.warehousemanagement.wms.purchases.model.dto;


import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDTO {
    private Integer purchaseId;
    private Integer userId;
    private Integer supplierId;    // 추가: 거래처 ID 필드
    private LocalDate purchaseDate;
    private LocalDate purchaseDueDate;
    private String purchaseStatus;
    private LocalDate purchaseUpdatedAt;
    private LocalDate purchaseCreatedAt;
    private String purchaseNotes;
    private Product product;
    private Supplier supplier;


    private List<PurchaseItemDTO> items = new ArrayList<>();


//    private Supplier supplier;
//    private Storages storages;


    public PurchaseDTO() {}


    public PurchaseDTO(Integer purchaseId, Integer userId,LocalDate purchaseCreatedAt ,LocalDate purchaseDate, LocalDate purchaseDueDate, String purchaseStatus, LocalDate purchaseUpdatedAt,String purchaseNotes
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
    
    // 거래처 ID를 포함하는 생성자 추가
    public PurchaseDTO(Integer purchaseId, Integer userId, Integer supplierId, LocalDate purchaseCreatedAt, 
                    LocalDate purchaseDate, LocalDate purchaseDueDate, String purchaseStatus, 
                    LocalDate purchaseUpdatedAt, String purchaseNotes) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.supplierId = supplierId;
        this.purchaseDate = purchaseDate;
        this.purchaseDueDate = purchaseDueDate;
        this.purchaseStatus = purchaseStatus;
        this.purchaseUpdatedAt = purchaseUpdatedAt;
        this.purchaseCreatedAt = purchaseCreatedAt;
        this.purchaseNotes = purchaseNotes;
    }
    
    public  PurchaseDTO(Supplier Supplier){
        this.supplier = Supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
    
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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
                ", supplierId=" + supplierId +
                ", purchaseDate=" + purchaseDate +
                ", purchaseDueDate=" + purchaseDueDate +
                ", purchaseStatus='" + purchaseStatus + '\'' +
                ", purchase_updated_at=" + purchaseUpdatedAt +
                ", purchase_created_at=" + purchaseCreatedAt +
                ", purchase_notes='" + purchaseNotes + '\'' +
                ", supplier=" + supplier +
                ", items=" + items +
//                ", storages=" + storages +
                '}';
    }
}
