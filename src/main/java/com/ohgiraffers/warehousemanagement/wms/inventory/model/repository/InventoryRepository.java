package com.ohgiraffers.warehousemanagement.wms.inventory.model.repository;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findTopByProductProductIdOrderByInventoryExpiryDateAsc(Integer productId);

    // 재고 조회 리스트에서 '상세 정보' 를 눌렀을 때, 해당 제품 이름의 재고 목록을 보여준다. (유통기한이 빠른 순)
    Page<Inventory> findByProductProductIdOrderByInventoryExpiryDateAsc(Long productId, Pageable pageable);


    // 페이징네이션 구현 - 상품명 검색 조회
    @Query("SELECT new com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO(" +
            "i.product.productId, i.product.productName, COUNT(i), SUM(i.availableStock), " +
            "SUM(i.allocatedStock), SUM(i.disposedStock)) " +
            "FROM Inventory i " +
            "WHERE i.product.productName LIKE :productName " +
            "GROUP BY i.product.productName, i.product.productId")
    Page<InventoryViewDTO> findInventoryViewDTOByProductName(String productName, Pageable pageable);

    // 페이징네이션 구현 - 전체 목록 조회
    @Query("SELECT new com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO(" +
            "i.product.productId, i.product.productName, COUNT(i), SUM(i.availableStock), " +
            "SUM(i.allocatedStock), SUM(i.disposedStock)) " +
            "FROM Inventory i " +
            "GROUP BY i.product.productName, i.product.productId")
    Page<InventoryViewDTO> getInventoryViewListWithPaging(Pageable pageable);


    @Query("SELECT MAX(i.lotNumber) FROM Inventory i WHERE i.product.productId = :productId AND i.lotNumber LIKE :datePrefix")
    String findMaxLotNumberByProductAndDate(Integer productId, String datePrefix);

    //상품 ID와 로트 번호로 재고 조회.
    Optional<Inventory> findByProductProductIdAndLotNumber(Integer productId, String lotNumber);

}