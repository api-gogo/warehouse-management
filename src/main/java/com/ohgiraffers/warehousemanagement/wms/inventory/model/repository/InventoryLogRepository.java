package com.ohgiraffers.warehousemanagement.wms.inventory.model.repository;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {

    List<InventoryLog> findByInventoryOrderByInventoryLogCreatedDesc(Inventory inventory);

}
