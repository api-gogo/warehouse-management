package com.ohgiraffers.warehousemanagement.wms.product.model.entity;

import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 정보를 나타내는 엔티티 클래스
 * 상품의 기본 정보, 카테고리, 거래처, 담당자, 재고와의 관계를 정의
 */
@Entity
@Table(name = "products")
public class Product {

    // 상품의 고유 식별자 (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    // 상품이 속한 카테고리와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // 상품의 거래처와의 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    // 상품의 담당자 ID (User 엔티티와 외래 키 관계 없음)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 상품 이름
    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;

    // 상품의 유통기한 (일 단위)
    @Column(name = "expiration_date", nullable = false)
    private Integer expirationDate;

    // 상품의 보관 방법
    @Column(name = "storage_method", length = 20, nullable = false)
    private String storageMethod;

    // 박스당 단가
    @Column(name = "price_per_box", nullable = false)
    private Integer pricePerBox;

    // 박스당 상품 개수
    @Column(name = "quantity_per_box", nullable = false)
    private Integer quantityPerBox;

    // 상품 등록 시간
    @Column(name = "product_created_at", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime productCreatedAt;

    // 상품 수정 시간
    @CreationTimestamp
    @Column(name = "product_updated_at", columnDefinition = "DATETIME")
    private LocalDateTime productUpdatedAt;

    // 상품 삭제 시간
    @CreationTimestamp
    @Column(name = "product_deleted_at", columnDefinition = "DATETIME")
    private LocalDateTime productDeletedAt;

    // 상품 삭제 여부 (논리적 삭제)
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // 상품 상태 (대기, 승인, 거절, 삭제)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'APPROVED'")
    private ProductStatus status;

    // 상품과 관련된 재고 목록과의 일대다 관계
    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories = new ArrayList<>();

    // 상품 상태를 정의하는 ENUM
    public enum ProductStatus {
        PENDING_CREATE, PENDING_UPDATE, PENDING_DELETE, APPROVED, REJECTED, DELETED
    }

    public Product() {}

    public Product(Integer productId, Category category, Supplier supplier, Long userId, String productName, Integer expirationDate, String storageMethod, Integer pricePerBox, Integer quantityPerBox, LocalDateTime productCreatedAt, LocalDateTime productUpdatedAt, LocalDateTime productDeletedAt, Boolean isDeleted, ProductStatus status) {
        this.productId = productId;
        this.category = category;
        this.supplier = supplier;
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
        this.status = status;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", category=" + category +
                ", supplier=" + supplier +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", expirationDate=" + expirationDate +
                ", storageMethod='" + storageMethod + '\'' +
                ", pricePerBox=" + pricePerBox +
                ", quantityPerBox=" + quantityPerBox +
                ", productCreatedAt=" + productCreatedAt +
                ", productUpdatedAt=" + productUpdatedAt +
                ", productDeletedAt=" + productDeletedAt +
                ", isDeleted=" + isDeleted +
                ", status=" + status +
                '}';
    }
}