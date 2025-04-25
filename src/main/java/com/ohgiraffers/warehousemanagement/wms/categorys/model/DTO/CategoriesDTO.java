package com.ohgiraffers.warehousemanagement.wms.categorys.model.DTO;

import java.time.LocalDateTime;

public class CategoriesDTO {

    private Long categoryId;
    private String categoryName;
    private Long parentId;
    private Integer level;
    private LocalDateTime categoryCreatedAt;
    private LocalDateTime categoryUpdatedAt;

    // 기본 생성자
    public CategoriesDTO() {
    }

    // 전체 생성자
    public CategoriesDTO(Long categoryId, String categoryName, Long parentId,
                         Integer level, LocalDateTime categoryCreatedAt,
                         LocalDateTime categoryUpdatedAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.level = level;
        this.categoryCreatedAt = categoryCreatedAt;
        this.categoryUpdatedAt = categoryUpdatedAt;
    }

    // Getter
    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getParentId() {
        return parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public LocalDateTime getCategoryCreatedAt() {
        return categoryCreatedAt;
    }

    public LocalDateTime getCategoryUpdatedAt() {
        return categoryUpdatedAt;
    }

    // Setter
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setCategoryCreatedAt(LocalDateTime categoryCreatedAt) {
        this.categoryCreatedAt = categoryCreatedAt;
    }

    public void setCategoryUpdatedAt(LocalDateTime categoryUpdatedAt) {
        this.categoryUpdatedAt = categoryUpdatedAt;
    }
}