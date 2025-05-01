package com.ohgiraffers.warehousemanagement.wms.product.service;

import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;

import java.util.List;

public interface ProductService {

    // 특정 상품 조회 (DTO 반환)
    ProductResponseDTO getProductById(Integer id);

    // 특정 상품 조회 (엔티티 반환)
    Product findProductById(Integer productId);

    // 모든 활성 거래처 목록 조회 (SupplierDTO 리스트로 반환)
    List<SupplierDTO> getSuppliers();

}
