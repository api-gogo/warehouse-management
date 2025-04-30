package com.ohgiraffers.warehousemanagement.wms.inventory.model.repository;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findTopByProductProductIdOrderByInventoryExpiryDateAsc(Integer productId);

    // 재고 조회 리스트에서 '상세 정보' 를 눌렀을 때, 해당 제품 이름의 재고 목록을 보여준다.
    List<Inventory> findByProductProductIdOrderByInventoryExpiryDateAsc(Integer productId);


    @Query("SELECT MAX(i.lotNumber) FROM Inventory i WHERE i.product.productId = :productId AND i.lotNumber LIKE :datePrefix")
    String findMaxLotNumberByProductAndDate(Integer productId, String datePrefix);


    @Query("SELECT new com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO(" +
            "i.product.productId, i.product.productName, COUNT(i), SUM(i.availableStock), SUM(i.allocatedStock), SUM(i.disposedStock)) " +
            "FROM Inventory i GROUP BY i.product.productName, i.product.productId")
    List<InventoryViewDTO> groupByProductName();

    @Query("SELECT new com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO(\n" +
            "i.product.productId, i.product.productName, COUNT(i), SUM(i.availableStock), SUM(i.allocatedStock), SUM(i.disposedStock))\n" +
            "FROM Inventory i \n" +
            "WHERE i.product.productName LIKE :productName\n" +
            "GROUP BY i.product.productName, i.product.productId")
    List<InventoryViewDTO> findgroupByProductName(String productName);
}
