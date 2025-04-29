package com.ohgiraffers.warehousemanagement.wms.inventory.model.repository;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductProductId(Long productId);
    Optional<Inventory> findTopByProductProductIdOrderByInventoryExpiryDateAsc(Integer productId);

    @Query("SELECT MAX(i.lotNumber) FROM Inventory i WHERE i.product.productId = :productId AND i.lotNumber LIKE :datePrefix")
    String findMaxLotNumberByProductAndDate(Integer productId, String datePrefix);
}
