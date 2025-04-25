package com.ohgiraffers.warehousemanagement.wms.categorys.model.repository;

import com.ohgiraffers.warehousemanagement.wms.categorys.model.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {}