package com.ohgiraffers.warehousemanagement.wms.category.model.DTO;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class CategoryDTO {

    private Long categoryId;          // 카테고리 번호
    private String categoryName;      // 카테고리 이름
    private Long parentId;            // 상위 카테고리
    private Integer level;            // 계층 레벨 (대분류: 1, 중분류: 2)
    private LocalDateTime categoryCreatedAt; // 생성일자
    private LocalDateTime categoryUpdatedAt; // 수정일자

    // 생성자
    public CategoryDTO() {}

    public CategoryDTO(Long categoryId, String categoryName, Long parentId, Integer level,
                        LocalDateTime categoryCreatedAt, LocalDateTime categoryUpdatedAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.setLevel(level); // 검증 포함 setter 사용
        this.categoryCreatedAt = categoryCreatedAt;
        this.categoryUpdatedAt = categoryUpdatedAt;
    }

    // Getter와 Setter
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
        // level 값이 1 또는 2인지 검증
        if (level == null || (level != 1 && level != 2)) {
            throw new IllegalArgumentException("Level must be 1 (대분류) or 2 (중분류)");
        }
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

    // toString
    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", level=" + level +
                ", categoryCreatedAt=" + categoryCreatedAt +
                ", categoryUpdatedAt=" + categoryUpdatedAt +
                '}';
    }
}