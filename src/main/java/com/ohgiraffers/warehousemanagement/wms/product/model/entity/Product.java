package com.ohgiraffers.warehousemanagement.wms.product.model.entity;

import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Where(clause = "is_deleted = false") // 삭제상태 -> 1인 경우만 조회가능
public class Product {

    //상품번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer productId;
    //카테고리 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    //거래처 ID
    //    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 설정
    //    @JoinColumn(name = "supplier_id", nullable = false) // 외래 키
    //    private Supplier supplier;

    //아직 거래처 외래키가 만들어지지 않아서 임시생성
    @Column(name = "supplier_id" , nullable = false)
    private Integer supplierId;

    //담당자
    @Column(name = "user_id", nullable = false)
    private Integer user_id;

    //품명
    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;

    //유통기한
    @Column(name = "expiration_date", nullable = false)
    private Integer expirationDate;

    //보관방법
    @Column(name = "storage_method", length = 20, nullable = false)
    private String storageMethod;

    //박스당 단가
    @Column(name = "price_per_box", nullable = false)
    private Integer pricePerBox;

    //박스당 개수
    @Column(name = "quantity_per_box", nullable = false)
    private Integer quantityPerBox;

    //등록시간
    @Column(name = "product_created_at", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime productCreatedAt;

    //수정시간
    @CreationTimestamp
    @Column(name = "product_updated_at", columnDefinition = "DATETIME")
    private LocalDateTime productUpdatedAt;

    //삭제시간
    @CreationTimestamp
    @Column(name = "product_deleted_at", columnDefinition = "DATETIME")
    private LocalDateTime productDeletedAt;

    //삭제상태
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;


    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories = new ArrayList<>();

    //생성자
    public Product() {}

    public Product(Integer productId, Category category, Integer supplierId, Integer user_id, String productName, Integer expirationDate, String storageMethod, Integer pricePerBox, Integer quantityPerBox, LocalDateTime productCreatedAt, LocalDateTime productUpdatedAt, LocalDateTime productDeletedAt, Boolean isDeleted) {
        this.productId = productId;
        this.category = category;
        this.supplierId = supplierId;
        this.user_id = user_id;
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

    //게터 , 세터
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

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    //toString

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", category=" + category +
                ", supplierId=" + supplierId +
                ", user_id=" + user_id +
                ", productName='" + productName + '\'' +
                ", expirationDate=" + expirationDate +
                ", storageMethod='" + storageMethod + '\'' +
                ", pricePerBox=" + pricePerBox +
                ", quantityPerBox=" + quantityPerBox +
                ", productCreatedAt=" + productCreatedAt +
                ", productUpdatedAt=" + productUpdatedAt +
                ", productDeletedAt=" + productDeletedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
