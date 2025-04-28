package com.ohgiraffers.warehousemanagement.wms.category.model.entity;

import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "category_name", length = 50, nullable = false)
    private String categoryName;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "category_created_at", nullable = false)
    private LocalDateTime categoryCreatedAt;

    @Column(name = "category_updated_at")
    private LocalDateTime categoryUpdatedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;

    public Category() {}

    public Category(Integer categoryId, String categoryName, Integer parentId, Integer level, LocalDateTime categoryCreatedAt, LocalDateTime categoryUpdatedAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.level = level;
        this.categoryCreatedAt = categoryCreatedAt;
        this.categoryUpdatedAt = categoryUpdatedAt;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public LocalDateTime getCategoryCreatedAt() {
        return categoryCreatedAt;
    }

    public void setCategoryCreatedAt(LocalDateTime categoryCreatedAt) {
        this.categoryCreatedAt = categoryCreatedAt;
    }

    public LocalDateTime getCategoryUpdatedAt() {
        return categoryUpdatedAt;
    }

    public void setCategoryUpdatedAt(LocalDateTime categoryUpdatedAt) {
        this.categoryUpdatedAt = categoryUpdatedAt;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", level=" + level +
                ", categoryCreatedAt=" + categoryCreatedAt +
                ", categoryUpdatedAt=" + categoryUpdatedAt +
                '}';
    }
}