package com.ohgiraffers.warehousemanagement.wms.purchases.model.dto;

public class PurchaseItemDTO {
    private Integer purchaseItemId;
    private Integer purchaseId;
    private Integer productId;
    private Integer productQuantity;


    public PurchaseItemDTO(Integer purchaseItemId, Integer purchaseId, Integer productId, Integer productQuantity) {
        this.purchaseItemId = purchaseItemId;
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public Integer getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Integer purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public String toString() {
        return "PurchaseItemDTO{" +
                "purchaseItemId=" + purchaseItemId +
                ", purchaseId=" + purchaseId +
                ", productId=" + productId +
                ", productQuantity=" + productQuantity +
                '}';
    }
}
