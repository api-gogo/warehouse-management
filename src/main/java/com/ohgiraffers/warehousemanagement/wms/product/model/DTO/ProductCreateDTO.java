package com.ohgiraffers.warehousemanagement.wms.product.model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ProductCreateDTO {

    private Integer productId;
    private Integer categoryId;
    private Integer supplierId;
    private Long userId;
    private String productName;
    private Integer expirationDate;
    private String storageMethod;
    private Integer pricePerBox;
    private Integer quantityPerBox;
    private LocalDateTime productCreatedAt;
    private Boolean isDeleted;

    public ProductCreateDTO() {}

    public ProductCreateDTO(Integer productId, Integer categoryId, Integer supplierId, Long userId, String productName,
                            Integer expirationDate, String storageMethod, Integer pricePerBox, Integer quantityPerBox,
                            LocalDateTime productCreatedAt, Boolean isDeleted) {
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
        this.isDeleted = isDeleted;
    }

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

    @NotNull(message = "공급업체 ID는 필수 입력 항목입니다.")
    @Min(value = 1, message = "공급업체 ID는 1 이상이어야 합니다.")
    @Max(value = 2147483647, message = "공급업체 ID는 2,147,483,647을 초과할 수 없습니다.")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("품명은 null이거나 비어 있을 수 없습니다.");
        }
        if (productName.length() > 50) {
            throw new IllegalArgumentException("품명은 50자를 초과할 수 없습니다.");
        }
        this.productName = productName;
    }

    public Integer getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Integer expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("유통기한은 null일 수 없습니다.");
        }
        this.expirationDate = expirationDate;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    public void setStorageMethod(String storageMethod) {
        if (storageMethod == null || storageMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("보관방법은 null이거나 비어 있을 수 없습니다.");
        }
        if (storageMethod.length() > 20) {
            throw new IllegalArgumentException("보관방법은 20자를 초과할 수 없습니다.");
        }
        this.storageMethod = storageMethod;
    }

    @NotNull(message = "박스당 단가는 필수 입력 항목입니다.")
    @Min(value = 0, message = "박스당 단가는 음수일 수 없습니다.")
    @Max(value = 2147483647, message = "박스당 단가는 2,147,483,647을 초과할 수 없습니다.")
    public Integer getPricePerBox() {
        return pricePerBox;
    }

    public void setPricePerBox(Integer pricePerBox) {
        this.pricePerBox = pricePerBox;
    }

    @NotNull(message = "박스당 개수는 필수 입력 항목입니다.")
    @Min(value = 1, message = "박스당 개수는 1보다 작을 수 없습니다.")
    @Max(value = 2147483647, message = "박스당 개수는 2,147,483,647을 초과할 수 없습니다.")
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "ProductCreateDTO{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", supplierId=" + supplierId +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", expirationDate=" + expirationDate +
                ", storageMethod='" + storageMethod + '\'' +
                ", pricePerBox=" + pricePerBox +
                ", quantityPerBox=" + quantityPerBox +
                ", productCreatedAt=" + productCreatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}