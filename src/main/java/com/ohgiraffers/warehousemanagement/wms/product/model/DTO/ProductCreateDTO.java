package com.ohgiraffers.warehousemanagement.wms.product.model.DTO;

/*
* product_id
* product_created_at,
* product_updated_at,
* product_deleted_at,
* is_deleted
* 를 제외한 상품 추가용 DTO
* **/
public class ProductCreateDTO {

    private Integer categoryId; // 카테고리 번호
    private Integer supplierId; // 거래처 ID
    private Integer userId; // 담당자
    private String productName; // 품명
    private Integer expirationDate; // 유통기한
    private String storageMethod; // 보관방법
    private Integer pricePerBox; // 박스당 단가
    private Integer quantityPerBox; // 박스당 개수

    // 생성자
    public ProductCreateDTO() {}

    public ProductCreateDTO(Integer categoryId, Integer supplierId, Integer userId, String productName,
                            Integer expirationDate, String storageMethod, Integer pricePerBox, Integer quantityPerBox) {
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.userId = userId;
        this.productName = productName;
        this.expirationDate = expirationDate;
        this.storageMethod = storageMethod;
        this.pricePerBox = pricePerBox;
        this.quantityPerBox = quantityPerBox;
    }

    // Getter Setter
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

    public Integer getPricePerBox() {
        return pricePerBox;
    }

    public void setPricePerBox(Integer pricePerBox) {
        if (pricePerBox == null) {
            throw new IllegalArgumentException("박스당 단가는 null일 수 없습니다.");
        }
        this.pricePerBox = pricePerBox;
    }

    public Integer getQuantityPerBox() {
        return quantityPerBox;
    }

    public void setQuantityPerBox(Integer quantityPerBox) {
        if (quantityPerBox == null) {
            throw new IllegalArgumentException("박스당 개수는 null일 수 없습니다.");
        }
        this.quantityPerBox = quantityPerBox;
    }

    @Override
    public String toString() {
        return "ProductCreateDTO{" +
                "categoryId=" + categoryId +
                ", supplierId=" + supplierId +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", expirationDate=" + expirationDate +
                ", storageMethod='" + storageMethod + '\'' +
                ", pricePerBox=" + pricePerBox +
                ", quantityPerBox=" + quantityPerBox +
                '}';
    }
}