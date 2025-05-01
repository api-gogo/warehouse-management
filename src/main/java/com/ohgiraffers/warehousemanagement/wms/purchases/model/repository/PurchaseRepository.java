package com.ohgiraffers.warehousemanagement.wms.purchases.model.repository;

import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseItemDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface  PurchaseRepository extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByPurchaseStatus(PurchaseStatus purchaseStatus);
    List<Purchase> findByUserId(Integer userId);
    List<Purchase> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);
    Page<Purchase> findAll(Pageable pageable);

    // 검색 조건이 있는 경우의 페이징 메소드
    Page<Purchase> findByPurchaseStatusContaining(String status, Pageable pageable);

    // 복합 검색 조건 (AND 조건)
    Page<Purchase> findByPurchaseDateBetweenAndPurchaseStatusContaining(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String status,
            Pageable pageable
    );


     // 직접적으로 쿼리를 작성해서 상품 id 상품이름, 단가를 가져옴
    @Query(value = """
        SELECT
            p.product_id AS productId,
            p.product_name AS productName,
            p.price_per_box AS pricePerBox,
            p.quantity_per_box AS quantityPerBox
        FROM products p
        WHERE p.product_id = :productId
        """, nativeQuery = true)
    PurchaseItemDTO findProductInfo(@Param("productId") Integer productId);
//    @Query(value = """
//    SELECT
//        p.product_id AS productId,
//        p.product_name AS productName,
//        p.price_per_box AS pricePerBox,
//        p.quantity_per_box AS quantityPerBox
//    FROM products p
//    WHERE p.product_id = :productId
//    """, nativeQuery = true)
//    PurchaseItemDTO findProductInfoById(@Param("productId") Integer productId);
//

}
