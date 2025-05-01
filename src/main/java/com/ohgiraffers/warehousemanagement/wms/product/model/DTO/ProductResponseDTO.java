package com.ohgiraffers.warehousemanagement.wms.product.model.DTO;

import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;

import java.time.LocalDateTime;

public class ProductResponseDTO {

    private Integer productId;
    private Category category;
    private Integer supplierId;
    private String supplierName;
    private Long userId;
    private String userName;
    private String productName;
    private Integer expirationDate;
    private String storageMethod;
    private Integer pricePerBox;
    private Integer quantityPerBox;
    private LocalDateTime productCreatedAt;
    private LocalDateTime productUpdatedAt;
    private LocalDateTime productDeletedAt;
    private Boolean isDeleted;

    public ProductResponseDTO() {}

    public ProductResponseDTO(Integer productId, Category category, Integer supplierId, String supplierName, Long userId, String userName,
                              String productName, Integer expirationDate, String storageMethod,
                              Integer pricePerBox, Integer quantityPerBox, LocalDateTime productCreatedAt,
                              LocalDateTime productUpdatedAt, LocalDateTime productDeletedAt, Boolean isDeleted) {
        this.productId = productId;
        this.category = category;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.userId = userId;
        this.userName = userName;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return category != null ? category.getCategoryId() : null;
    }

    public void setCategoryId(Integer categoryId) {
        if (category == null) {
            category = new Category();
        }
        category.setCategoryId(categoryId);
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
                ", 카테고리 번호=" + (category != null ? category.getCategoryId() : null) +
                ", 거래처 ID=" + supplierId +
                ", 거래처명='" + supplierName + '\'' +
                ", 담당자 ID=" + userId +
                ", 담당자명='" + userName + '\'' +
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