package com.ohgiraffers.warehousemanagement.wms.product.model.DTO;

import java.time.LocalDateTime;

/*  전체조회용 DTO  */

public class ProductResponseDTO {

    private Integer productId; // 상품번호
    private Integer categoryId; // 카테고리 번호
    private Integer supplierId; // 거래처 ID
    private Integer userId; // 담당자
    private String productName; // 품명
    private Integer expirationDate; // 유통기한
    private String storageMethod; // 보관방법
    private Integer pricePerBox; // 박스당 단가
    private Integer quantityPerBox; // 박스당 개수
    private LocalDateTime productCreatedAt; // 등록시간
    private LocalDateTime productUpdatedAt; // 수정시간
    private LocalDateTime productDeletedAt; // 삭제시간
    private Boolean isDeleted; // 삭제상태

    // 생성자
    public ProductResponseDTO() {}

    public ProductResponseDTO(Integer productId, Integer categoryId, Integer supplierId, Integer userId,
                                String productName, Integer expirationDate, String storageMethod,
                                Integer pricePerBox, Integer quantityPerBox, LocalDateTime productCreatedAt,
                                LocalDateTime productUpdatedAt, LocalDateTime productDeletedAt, Boolean isDeleted) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.userId = userId;
        this.productName = productName;
        this.expirationDate = expirationDate;
        this.storageMethod = storageMethod;
        this.pricePerBox = pricePerBox;
        this.quantityPerBox = quantityPerBox;
        this.productCreatedAt = productCreatedAt;
        this.productUpdatedAt = productUpdatedAt;
        this.productDeletedAt = productDeletedAt;
        this.isDeleted = isDeleted;
    }

    // Getter와 Setter
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Integer expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    public void setStorageMethod(String storageMethod) {
        this.storageMethod = storageMethod;
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

    public LocalDateTime getProductCreatedAt() {
        return productCreatedAt;
    }

    public void setProductCreatedAt(LocalDateTime productCreatedAt) {
        this.productCreatedAt = productCreatedAt;
    }

    public LocalDateTime getProductUpdatedAt() {
        return productUpdatedAt;
    }

    public void setProductUpdatedAt(LocalDateTime productUpdatedAt) {
        this.productUpdatedAt = productUpdatedAt;
    }

    public LocalDateTime getProductDeletedAt() {
        return productDeletedAt;
    }

    public void setProductDeletedAt(LocalDateTime productDeletedAt) {
        this.productDeletedAt = productDeletedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "ProductResponseDTO{" +
                "상품번호=" + productId +
                ", 카테고리 번호=" + categoryId +
                ", 거래처 ID=" + supplierId +
                ", 담당자=" + userId +
                ", 품명='" + productName + '\'' +
                ", 유통기한=" + expirationDate +
                ", 보관방법='" + storageMethod + '\'' +
                ", 박스당 단가=" + pricePerBox +
                ", 박스당 개수=" + quantityPerBox +
                ", 등록시간=" + productCreatedAt +
                ", 수정시간=" + productUpdatedAt +
                ", 삭제시간=" + productDeletedAt +
                ", 삭제상태=" + isDeleted +
                '}';
    }
}