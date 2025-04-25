package com.ohgiraffers.warehousemanagement.wms.products.model.repository;

import com.ohgiraffers.warehousemanagement.wms.products.model.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long> {
}
