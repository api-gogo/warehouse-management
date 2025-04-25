package com.ohgiraffers.warehousemanagement.categorys.model.repository;

import com.ohgiraffers.warehousemanagement.categorys.model.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {}