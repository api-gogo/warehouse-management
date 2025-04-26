package com.ohgiraffers.warehousemanagement.wms.inventory.service;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryDTO> findAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(inventory -> new InventoryDTO(
                        inventory.getInventoryid(),
                        inventory.getStorageId(),
                        inventory.getProductId(),
                        inventory.getLotNumber(),
                        inventory.getLocationCode(),
                        inventory.getAvailableStock(),
                        inventory.getAllocatedStock(),
                        inventory.getDisposedStock(),
                        inventory.getInventoryExpiryDate(),
                        inventory.getInventoryCreatedAt(),
                        inventory.getInventoryUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<InventoryDTO> findAllInventoriesByProductId(String productId) {
        try {
            Long searchProductId = Long.parseLong(productId);
            List<Inventory> findInventories = inventoryRepository.findByProductId(searchProductId);
            return findInventories.stream()
                    .map(inventory -> new InventoryDTO(
                            inventory.getInventoryid(),
                            inventory.getStorageId(),
                            inventory.getProductId(),
                            inventory.getLotNumber(),
                            inventory.getLocationCode(),
                            inventory.getAvailableStock(),
                            inventory.getAllocatedStock(),
                            inventory.getDisposedStock(),
                            inventory.getInventoryExpiryDate(),
                            inventory.getInventoryCreatedAt(),
                            inventory.getInventoryUpdatedAt()
                    ))
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }
}
