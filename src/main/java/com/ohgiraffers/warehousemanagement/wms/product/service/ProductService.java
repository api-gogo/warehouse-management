package com.ohgiraffers.warehousemanagement.wms.product.service;

import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;

public interface ProductService {

    // 특정 상품 조회 (DTO 반환)
    ProductResponseDTO getProductById(Integer id);

    // 특정 상품 조회 (엔티티 반환)
    Product findProductById(Integer productId);
}

/*
* //findProductById 테스트 코드
* @Autowired
private ProductService productService;

public void test() {
    Product product = productService.findProductById(1);
    System.out.println("상품 엔티티: " + product);
}
* */