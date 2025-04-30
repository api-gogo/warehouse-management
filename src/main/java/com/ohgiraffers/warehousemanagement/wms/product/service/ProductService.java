package com.ohgiraffers.warehousemanagement.wms.product.service;

import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;

public interface ProductService {

    // 특정 상품 조회 (DTO 반환)
    ProductResponseDTO getProductById(Integer id);

    // 특정 상품 조회 (엔티티 반환)
    Product findProductById(Integer productId);
}
