package com.ohgiraffers.warehousemanagement.wms.storage.service;

import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.request.StorageRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response.StorageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.entity.Storage;
import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
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

    // 전체 입고 목록 조회
    public List<StorageResponseDTO> getAllStorage() {
        List<Storage> storages = storageRepository.findAll();
        return storages.stream()
                .map(this::convertToStorageResponseDTO)
                .collect(Collectors.toList());
    }

    // 입고 ID로 조회
    public StorageResponseDTO getStorageById(int storageId) {
        Optional<Storage> optionalStorage = storageRepository.findById(storageId);
        if (optionalStorage.isPresent()) {
            return convertToStorageResponseDTO(optionalStorage.get());
        } else {
            throw new RuntimeException("입고 ID를 찾을 수 없습니다: " + storageId);
        }
    }

    // 입고 등록
    public StorageResponseDTO createStorage(StorageRequestDTO storageRequestDTO) {
        Storage storage = new Storage();
        storage.setStorageDate(storageRequestDTO.getStorageDate());
        storage.setStorageStatus(StorageStatus.WAITING);
        storage.setStorageReason(storageRequestDTO.getStorageReason()); // 추가된 필드 반영
        storage.setPurchaseId(storageRequestDTO.getPurchaseId());  // 발주 ID 추가

        Storage savedStorage = storageRepository.save(storage);
        return convertToStorageResponseDTO(savedStorage);
    }

    // 입고 수정
    public StorageResponseDTO updateStorage(int storageId, StorageRequestDTO storageRequestDTO) {
        Optional<Storage> optionalStorage = storageRepository.findById(storageId);
        if (optionalStorage.isPresent()) {
            Storage storage = optionalStorage.get();
            storage.setStorageDate(storageRequestDTO.getStorageDate());
            storage.setStorageStatus(storageRequestDTO.getStorageStatus());  // 수정된 상태 반영
            storage.setStorageReason(storageRequestDTO.getStorageReason());
            storage.setPurchaseId(storageRequestDTO.getPurchaseId());  // 발주 ID 반영

            Storage updatedStorage = storageRepository.save(storage);
            return convertToStorageResponseDTO(updatedStorage);
        } else {
            throw new RuntimeException("입고 ID를 찾을 수 없습니다: " + storageId);
        }
    }

    // 입고 삭제
    public void deleteStorage(int storageId) {
        if (storageRepository.existsById(storageId)) {
            storageRepository.deleteById(storageId);
        } else {
            throw new RuntimeException("입고 ID를 찾을 수 없습니다: " + storageId);
        }
    }

    // 엔티티를 DTO로 변환
    private StorageResponseDTO convertToStorageResponseDTO(Storage storage) {
        StorageResponseDTO dto = new StorageResponseDTO();
        dto.setStorageId(storage.getStorageId());
        dto.setPurchaseId(storage.getPurchaseId());
        dto.setStorageDate(storage.getStorageDate());
        dto.setStorageStatus(storage.getStorageStatus());
        dto.setStorageReason(storage.getStorageReason());
        dto.setPurchaseId(storage.getPurchaseId());  // 발주 ID 반영
        return dto;
    }

    public Integer getPurchaseId(int storageId) {
        Optional<Storage> optionalStorage = storageRepository.findById(storageId);
        if (optionalStorage.isPresent()) {
            return optionalStorage.get().getPurchaseId();
        }
        return null;
    }

}
