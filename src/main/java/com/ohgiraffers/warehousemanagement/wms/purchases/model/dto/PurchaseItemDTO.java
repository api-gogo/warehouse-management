package com.ohgiraffers.warehousemanagement.wms.purchases.model.dto;

import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class PurchaseItemDTO {

    private Integer purchaseItemId;
    private Integer purchaseId;
    private Integer productId;
    private Integer productQuantity;
    private Product product;
    private String productName;
    private Integer pricePerBox;
    private Integer quantityPerBox;

    private Integer price; // pricePerBox와 매핑
    private Integer quantity;

    // 쿼리 결과를 매핑하기 위한 생성자 (Native Query 사용)
    public PurchaseItemDTO(Integer productId, String productName, Integer pricePerBox, Integer quantityPerBox) {
        this.productId = productId;
        this.productName = productName;
        this.pricePerBox = pricePerBox;
        this.quantityPerBox = quantityPerBox;
    }
    public PurchaseItemDTO(Integer purchaseItemId, Integer purchaseId, Integer productId, Integer productQuantity,
                           Product product, String productName, Integer pricePerBox, Integer quantityPerBox) {
        this.purchaseItemId = purchaseItemId;
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.product = product;
        this.productName = productName;
        this.pricePerBox = pricePerBox;
        this.quantityPerBox = quantityPerBox;
    }
    public PurchaseItemDTO() {

    }

    public Integer getPrice() {
        return pricePerBox;
    }

    public void setPrice(Integer price) {
        this.pricePerBox = price;
    }

    public Integer getQuantity() {
        return productQuantity;
    }

    public void setQuantity(Integer quantity) {
        this.productQuantity = quantity;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPricePerBox() {
        return pricePerBox;
    }

    public void setPricePerBox(Integer pricePerBox) {
        this.pricePerBox = pricePerBox;
    }

    public Integer getQuantityPerBox() {
        return quantityPerBox;
    }

    public void setQuantityPerBox(Integer quantityPerBox) {
        this.quantityPerBox = quantityPerBox;
    }

    @Override
    public String toString() {
        return "PurchaseItemDTO{" +
                "purchaseItemId=" + purchaseItemId +
                ", purchaseId=" + purchaseId +
                ", productId=" + productId +
                ", productQuantity=" + productQuantity +
                ", product=" + product +
                ", productName='" + productName + '\'' +
                ", pricePerBox=" + pricePerBox +
                ", quantityPerBox=" + quantityPerBox +
                '}';
    }
}
