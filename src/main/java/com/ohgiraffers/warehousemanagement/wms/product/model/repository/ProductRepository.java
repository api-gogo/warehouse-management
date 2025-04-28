package com.ohgiraffers.warehousemanagement.wms.product.model.repository;

import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {}