package com.ohgiraffers.warehousemanagement.wms.storage.service;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inspection.repository.InspectionRepository;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.repository.PurchaseRepository;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.request.StorageRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response.PurchaseInfoResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response.StorageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import com.ohgiraffers.warehousemanagement.wms.storage.model.entity.Storage;
import com.ohgiraffers.warehousemanagement.wms.storage.model.repository.StorageRepository;
import com.ohgiraffers.warehousemanagement.wms.supplier.repository.SupplierRepository;
import com.ohgiraffers.warehousemanagement.wms.user.repository.UserRepository;
import com.ohgiraffers.warehousemanagement.wms.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final PurchaseRepository purchaseRepository;
    private final InspectionRepository inspectionRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    @Autowired
    public StorageService(StorageRepository storageRepository,
                          PurchaseRepository purchaseRepository,
                          InspectionRepository inspectionRepository,
                          SupplierRepository supplierRepository,
                          UserRepository userRepository,
                          InventoryService inventoryService) {
        this.storageRepository = storageRepository;
        this.purchaseRepository = purchaseRepository;
        this.inspectionRepository = inspectionRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.inventoryService = inventoryService;
    }

    public List<StorageResponseDTO> getAllStorage() {
        return storageRepository.findAll().stream()
                .map(this::convertToStorageResponseDTO)
                .collect(Collectors.toList());
    }

    public StorageResponseDTO getStorageById(int id) {
        return storageRepository.findById(id)
                .map(this::convertToStorageResponseDTO)
                .orElseThrow(() -> new RuntimeException("입고 ID를 찾을 수 없습니다: " + id));
    }

    public StorageResponseDTO createStorage(StorageRequestDTO dto) {
        if (storageRepository.existsByPurchase_PurchaseId(dto.getPurchaseId())) {
            throw new IllegalArgumentException("이미 입고 등록된 발주 ID입니다.");
        }

        Purchase purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new RuntimeException("발주 ID를 찾을 수 없습니다: " + dto.getPurchaseId()));

        Storage storage = new Storage();
        storage.setPurchase(purchase);
        storage.setStorageStatus(dto.getStorageStatus());
        storage.setInspectionStatus(dto.getInspectionStatus());
        storage.setStorageDate(dto.getStorageDate());
        storage.setStorageReason(dto.getStorageReason());

        Storage saved = storageRepository.save(storage);

        //  입고 상태가 COMPLETED일 때 재고 트리거 호출
        if (dto.getStorageStatus() == StorageStatus.COMPLETED) {
            inventoryService.notifyStorageCompleted(dto.getPurchaseId());
        }

        return convertToStorageResponseDTO(saved);
    }

    public StorageResponseDTO updateStorage(int id, StorageRequestDTO dto) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("입고 ID를 찾을 수 없습니다: " + id));

        Purchase purchase = purchaseRepository.findById(dto.getPurchaseId())
                .orElseThrow(() -> new RuntimeException("발주 ID를 찾을 수 없습니다: " + dto.getPurchaseId()));

        storage.setPurchase(purchase);
        storage.setStorageStatus(dto.getStorageStatus());
        storage.setInspectionStatus(dto.getInspectionStatus());
        storage.setStorageDate(dto.getStorageDate());
        storage.setStorageReason(dto.getStorageReason());

        Storage updated = storageRepository.save(storage);

        //  입고 상태가 COMPLETED일 때 재고 트리거 호출
        if (dto.getStorageStatus() == StorageStatus.COMPLETED) {
            inventoryService.notifyStorageCompleted(dto.getPurchaseId());
        }

        return convertToStorageResponseDTO(updated);
    }

    public void deleteStorage(int id) {
        if (!storageRepository.existsById(id)) {
            throw new RuntimeException("입고 ID를 찾을 수 없습니다: " + id);
        }
        storageRepository.deleteById(id);
    }

    public List<StorageResponseDTO> searchStoragesByPurchaseId(String searchKeyword) {
        try {
            Integer purchaseId = Integer.parseInt(searchKeyword);
            List<Storage> storages = storageRepository.findByPurchase_PurchaseId(purchaseId);
            return storages.stream()
                    .map(this::convertToStorageResponseDTO)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    public boolean isDuplicatePurchaseId(Integer purchaseId) {
        return storageRepository.existsByPurchase_PurchaseId(purchaseId);
    }

    public PurchaseInfoResponseDTO getPurchaseInfoById(Integer purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("해당 발주 ID를 찾을 수 없습니다: " + purchaseId));

        PurchaseInfoResponseDTO dto = new PurchaseInfoResponseDTO();
        dto.setPurchaseId(purchase.getPurchaseId());
        dto.setUserId(purchase.getUserId());

        if (purchase.getUserId() != null) {
            userRepository.findById(purchase.getUserId())
                    .ifPresent(user -> dto.setUserName(user.getUserName()));
        }

        inspectionRepository.findByTransactionTypeAndTransactionId(
                InspectionTransactionType.PURCHASE,
                Long.valueOf(purchase.getPurchaseId())
        ).ifPresent(inspection -> {
            dto.setInspectionStatus(inspection.getInspectionStatus().name());
        });

        if (purchase.getSupplier() != null) {
            dto.setSupplierName(purchase.getSupplier().getSupplierName());
        }

        dto.setPurchaseDueDate(purchase.getPurchaseDueDate());
        dto.setPurchaseStatus(purchase.getPurchaseStatus().name());
        dto.setItemCount(purchase.getItems() != null ? purchase.getItems().size() : 0);

        return dto;
    }

    private StorageResponseDTO convertToStorageResponseDTO(Storage entity) {
        StorageResponseDTO dto = new StorageResponseDTO();
        dto.setStorageId(entity.getStorageId());
        dto.setStorageStatus(entity.getStorageStatus());
        dto.setInspectionStatus(entity.getInspectionStatus());
        dto.setStorageDate(entity.getStorageDate());
        dto.setStorageReason(entity.getStorageReason());
        dto.setStorageCreatedAt(entity.getStorageCreatedAt());

        if (entity.getPurchase() != null) {
            Purchase purchase = entity.getPurchase();
            dto.setPurchaseId(purchase.getPurchaseId());
            dto.setPurchaseUserId(purchase.getUserId());
            dto.setPurchaseDueDate(purchase.getPurchaseDueDate());
            dto.setPurchaseStatus(purchase.getPurchaseStatus().name());

            if (purchase.getUserId() != null) {
                userRepository.findById(purchase.getUserId())
                        .ifPresent(user -> dto.setPurchaseUserName(user.getUserName()));
            }

            if (purchase.getSupplier() != null) {
                dto.setSupplierName(purchase.getSupplier().getSupplierName());
            }

            dto.setItemCount(purchase.getItems() != null ? purchase.getItems().size() : 0);

            inspectionRepository.findByTransactionTypeAndTransactionId(
                    InspectionTransactionType.PURCHASE,
                    Long.valueOf(purchase.getPurchaseId())
            ).ifPresent(inspection -> {
                dto.setInspectionStatus(inspection.getInspectionStatus().name());
            });
        }

        return dto;
    }

/*    public Integer getPurchaseId(int storageId) {
        Optional<Storage> optionalStorage = storageRepository.findById(storageId);
        if (optionalStorage.isPresent()) {
            return optionalStorage.get().getPurchaseId();
        }
        return null;
    }*/

}
