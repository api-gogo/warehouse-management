package com.ohgiraffers.warehousemanagement.wms.category.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
public class Category {

    //카테고리 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id" ,nullable = false)
    private Integer categoryId;

    //카테고리 이름
    @Column(name = "category_name", length = 50 , nullable = false)
    private String categoryName;
    //상위 카테고리
    @Column(name = "parent_id")
    private Integer parentId;
    //계층 레벨  대분류:1/중분류:2 <- 만 들어가게 설정하기 (DTO 계층에서)
    @Column(name = "level" , nullable = false)
    private Integer level; // <- 인티저? 롱? 이따 물어보기
    //생성일자
    @Column(name = "category_created_at" , nullable = false)
    private LocalDateTime categoryCreatedAt;
    //수정일자
    @Column(name = "category_updated_at")
    private LocalDateTime categoryUpdatedAt;

    //생성자
    public Category() {}

    public Category(Integer categoryId, String categoryName, Integer parentId, Integer level, LocalDateTime categoryCreatedAt, LocalDateTime categoryUpdatedAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.level = level;
        this.categoryCreatedAt = categoryCreatedAt;
        this.categoryUpdatedAt = categoryUpdatedAt;
    }
    //gatter setter
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
    //toString

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
