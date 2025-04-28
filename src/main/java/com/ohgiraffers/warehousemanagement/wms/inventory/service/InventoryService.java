package com.ohgiraffers.warehousemanagement.wms.inventory.service;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

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


    // DTO를 Entity로 변환
    public InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO result = new InventoryDTO(
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
        );
        return result;
    }


    public List<InventoryDTO> findAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<InventoryDTO> findAllInventoriesByProductId(Long productId) {
        // 실제 존재하는 상품 ID인지 확인
        Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("해당 상품의 재고가 존재하지 않습니다.");
        } else {
            return inventory.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public InventoryDTO findInventoryById(Long inventoryId) {
        Inventory findInventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new IllegalArgumentException("없는 재고입니다."));
        return convertToDTO(findInventory);
    }




    @Transactional
    public void updateInventory(Long inventoryId, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() ->
             new RuntimeException("없는 재고 데이터입니다."));

        inventory.setInventoryid(inventoryDTO.getInventoryId());
        inventory.setStorageId(inventoryDTO.getStorageId());
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setLotNumber(inventoryDTO.getLotNumber());
        inventory.setLocationCode(inventoryDTO.getLocationCode());
        inventory.setAvailableStock(inventoryDTO.getAvailableStock());
        inventory.setAllocatedStock(inventoryDTO.getAllocatedStock());
        inventory.setDisposedStock(inventoryDTO.getDisposedStock());
        inventory.setInventoryExpiryDate(inventoryDTO.getInventoryExpiryDate());
        // 생성일자는 변경하지 않음 (최초 생성 시점으로 유지)
        // 수정일자는 현재 시간으로 업데이트
        inventory.setInventoryUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void deleteInventory(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    @Transactional
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {

        // 현재 시간 설정
        LocalDateTime now = LocalDateTime.now();

        // ID를 제외하고 엔티티 생성 (setter 사용)
        Inventory inventory = new Inventory();
        inventory.setStorageId(inventoryDTO.getStorageId());
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setLotNumber(inventoryDTO.getLotNumber());
        inventory.setLocationCode(inventoryDTO.getLocationCode());
        inventory.setAvailableStock(inventoryDTO.getAvailableStock());
        inventory.setAllocatedStock(inventoryDTO.getAllocatedStock());
        inventory.setDisposedStock(inventoryDTO.getDisposedStock());
        inventory.setInventoryExpiryDate(inventoryDTO.getInventoryExpiryDate());

        // 생성 시간과 수정 시간을 현재 시간으로 설정
        inventory.setInventoryCreatedAt(now);
        inventory.setInventoryUpdatedAt(now);

        // 저장하고 저장된 엔티티 반환
        Inventory savedInventory = inventoryRepository.save(inventory);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return convertToDTO(savedInventory);
    }

}
