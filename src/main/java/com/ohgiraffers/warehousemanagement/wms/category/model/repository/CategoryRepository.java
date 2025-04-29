package com.ohgiraffers.warehousemanagement.wms.category.model.repository;

import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByLevel(Integer level);
    List<Category> findByParentId(Integer parentId);
}