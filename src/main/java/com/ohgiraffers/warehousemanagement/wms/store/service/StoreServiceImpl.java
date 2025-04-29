package com.ohgiraffers.warehousemanagement.wms.store.service;

import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;
import com.ohgiraffers.warehousemanagement.wms.store.model.entity.Store;
import com.ohgiraffers.warehousemanagement.wms.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {
    
    private final StoreRepository storeRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Page<StoreDTO> findAll(String search, String status, Pageable pageable) {
        boolean isDeleted = false;

        if (status != null) {
            if (status.equals("deleted")) {
                isDeleted = true;
            }
        }

        Page<Store> storePage = storeRepository.findByStatusAndSearch(isDeleted, search, pageable);

        return storePage.map(store -> new StoreDTO(
                store.getStoreId(),
                store.getStoreName(),
                store.getStoreAddress(),
                store.getStoreManagerName(),
                store.getStoreManagerPhone(),
                store.getStoreManagerEmail(),
                store.getStoreCreatedAt(),
                store.getStoreUpdatedAt(),
                store.getStoreDeletedAt(),
                store.getDeleted()
        ));
    }

    @Override
    public StoreDTO findById(Integer storeId) {
        Optional<Store> store = storeRepository.findByStoreId(storeId);

        return store.map(s -> new StoreDTO(
                s.getStoreId(),
                s.getStoreName(),
                s.getStoreAddress(),
                s.getStoreManagerName(),
                s.getStoreManagerPhone(),
                s.getStoreManagerEmail(),
                s.getStoreCreatedAt(),
                s.getStoreUpdatedAt(),
                s.getStoreDeletedAt(),
                s.getDeleted()
        )).orElse(null);
    }

    @Override
    public StoreDTO findByName(String storeName) {
        Optional<Store> store = storeRepository.findByStoreName(storeName);

        return store.map(s -> new StoreDTO(
                s.getStoreId(),
                s.getStoreName(),
                s.getStoreAddress(),
                s.getStoreManagerName(),
                s.getStoreManagerPhone(),
                s.getStoreManagerEmail(),
                s.getStoreCreatedAt(),
                s.getStoreUpdatedAt(),
                s.getStoreDeletedAt(),
                s.getDeleted()
        )).orElse(null);
    }

    public Integer createStore(StoreDTO storeDTO) {

        if (storeRepository.existsByStoreName(storeDTO.getStoreName())) {
            return -1;
        } else if (storeRepository.existsByStoreManagerPhone(storeDTO.getStoreManagerPhone())) {
            return -2;
        } else if (storeRepository.existsByStoreManagerEmail(storeDTO.getStoreManagerEmail())) {
            return -3;
        }

        try {
            Store store = new Store(
                    storeDTO.getStoreName(),
                    storeDTO.getStoreAddress(),
                    storeDTO.getStoreManagerName(),
                    storeDTO.getStoreManagerPhone(),
                    storeDTO.getStoreManagerEmail()
            );

            storeRepository.save(store);
            return store.getStoreId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}