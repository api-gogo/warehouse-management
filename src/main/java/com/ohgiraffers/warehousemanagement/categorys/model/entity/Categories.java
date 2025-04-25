package com.ohgiraffers.warehousemanagement.categorys.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorys")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 50)
    private String categoryName;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "level")
    private Integer level; // <- 인티저? 롱? 이따 물어보기

    @Column(name = "category_created_at")
    private LocalDateTime categoryCreatedAt;

    @Column(name = "category_updated_at")
    private LocalDateTime categoryUpdatedAt;

    public Categories() {
    }

    public Categories(Long categoryId, String categoryName, Long parentId, Integer level, LocalDateTime categoryCreatedAt, LocalDateTime categoryUpdatedAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.level = level;
        this.categoryCreatedAt = categoryCreatedAt;
        this.categoryUpdatedAt = categoryUpdatedAt;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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

    @Override
    public String toString() {
        return "Categorys{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", level=" + level +
                ", categoryCreatedAt=" + categoryCreatedAt +
                ", categoryUpdatedAt=" + categoryUpdatedAt +
                '}';
    }
}
