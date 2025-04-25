package com.ohgiraffers.warehousemanagement.products.model.repository;

import com.ohgiraffers.warehousemanagement.products.model.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long> {
}
