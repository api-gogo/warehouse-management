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

    @Query("SELECT MAX(i.lotNumber) FROM Inventory i WHERE i.product.productId = :productId AND i.lotNumber LIKE :datePrefix")
    String findMaxLotNumberByProductAndDate(Integer productId, String datePrefix);

    @Query("SELECT i FROM Inventory i WHERE i.product.productName LIKE :productName")
    List<Inventory> findByProductName(String productName);

    @Query("SELECT new com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO(" +
            "i.product.productName, COUNT(i), SUM(i.availableStock), SUM(i.allocatedStock), SUM(i.disposedStock)) " +
            "FROM Inventory i GROUP BY i.product.productName")
    List<InventoryViewDTO> groupByProductName();
}
