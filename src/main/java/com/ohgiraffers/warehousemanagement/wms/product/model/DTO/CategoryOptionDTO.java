package com.ohgiraffers.warehousemanagement.wms.product.model.DTO;

public class CategoryOptionDTO {
    private Integer id;
    private String name;
    private String parentName; // 대분류 이름 추가

    public CategoryOptionDTO(Integer id, String name, String parentName) {
        this.id = id;
        this.name = name;
        this.parentName = parentName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    // "대분류 - 중분류" 형식으로 표시하기 위한 메서드
    public String getFullName() {
        return parentName != null ? parentName + " - " + name : name;
    }
}