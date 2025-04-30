package com.ohgiraffers.warehousemanagement.wms.storage.service;

import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.request.StorageRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response.StorageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.entity.Storage;
import com.ohgiraffers.warehousemanagement.wms.storage.model.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private final StorageRepository storageRepository;

    @Autowired
    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
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
        Storage storage = new Storage();
        storage.setPurchaseId(dto.getPurchaseId());
        storage.setStorageStatus(dto.getStorageStatus());
        storage.setStorageDate(dto.getStorageDate());
        storage.setStorageReason(dto.getStorageReason());
        Storage saved = storageRepository.save(storage);
        return convertToStorageResponseDTO(saved);
    }

    public StorageResponseDTO updateStorage(int id, StorageRequestDTO dto) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("입고 ID를 찾을 수 없습니다: " + id));
        storage.setPurchaseId(dto.getPurchaseId());
        storage.setStorageStatus(dto.getStorageStatus());
        storage.setStorageDate(dto.getStorageDate());
        storage.setStorageReason(dto.getStorageReason());
        Storage updated = storageRepository.save(storage);
        return convertToStorageResponseDTO(updated);
    }

    public void deleteStorage(int id) {
        if (!storageRepository.existsById(id)) {
            throw new RuntimeException("입고 ID를 찾을 수 없습니다: " + id);
        }
        storageRepository.deleteById(id);
    }

    private StorageResponseDTO convertToStorageResponseDTO(Storage entity) {
        StorageResponseDTO dto = new StorageResponseDTO();
        dto.setStorageId(entity.getStorageId());
        dto.setPurchaseId(entity.getPurchaseId());
        dto.setStorageStatus(entity.getStorageStatus());
        dto.setStorageDate(entity.getStorageDate());
        dto.setStorageReason(entity.getStorageReason());
        return dto;
    }

    //  검색 기능
    public List<StorageResponseDTO> searchStoragesByPurchaseId(String searchKeyword) {
        try {
            Integer purchaseId = Integer.parseInt(searchKeyword);
            List<Storage> storages = storageRepository.findByPurchaseId(purchaseId);
            return storages.stream()
                    .map(this::convertToStorageResponseDTO)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }
}
