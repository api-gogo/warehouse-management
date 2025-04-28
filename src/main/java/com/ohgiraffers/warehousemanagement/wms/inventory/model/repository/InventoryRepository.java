package com.ohgiraffers.warehousemanagement.wms.inventory.model.repository;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByProductId(Long productId);
}
