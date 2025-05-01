package com.ohgiraffers.warehousemanagement.wms.product.model.repository;

import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * 주어진 상태 목록에 해당하는 상품 목록 조회
     * @param statuses 상태 목록 (예: PENDING_CREATE, PENDING_UPDATE)
     * @return 해당 상태의 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE p.status IN :statuses AND p.isDeleted = false")
    List<Product> findByStatusIn(List<Product.ProductStatus> statuses);

    /**
     * 논리적 삭제된 상품 목록 조회
     * @return 삭제된 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = true")
    List<Product> findByIsDeletedTrue();

    /**
     * 모든 상품 조회 (삭제 여부 및 상태 상관없이)
     * @return 모든 상품 목록
     */
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithAllStatuses();


    Product findProductByProductId(Integer productId);
}