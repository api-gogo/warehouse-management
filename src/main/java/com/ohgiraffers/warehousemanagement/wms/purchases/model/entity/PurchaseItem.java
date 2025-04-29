package com.ohgiraffers.warehousemanagement.wms.purchases.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "purchase_items")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_item_id")
    private Integer purchaseItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @Column(name = "product_id")
    private Integer productId;


    @Column(name = "product_quantity")
    private Integer productQuantity;

    public PurchaseItem() {}


    public PurchaseItem(Integer purchaseItemId, Purchase purchase, Integer productId, Integer productQuantity) {
        this.purchaseItemId = purchaseItemId;
        this.purchase = purchase;
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public Integer getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Integer purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
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
        return "PurchaseItem{" +
                "purchaseItemId=" + purchaseItemId +
                ", purchase=" + purchase +
                ", productId=" + productId +
                ", productQuantity=" + productQuantity +
                '}';
    }
}
