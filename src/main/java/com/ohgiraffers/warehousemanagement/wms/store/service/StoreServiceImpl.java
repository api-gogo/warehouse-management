package com.ohgiraffers.warehousemanagement.wms.store.service;

import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;
import com.ohgiraffers.warehousemanagement.wms.store.model.entity.Store;
import com.ohgiraffers.warehousemanagement.wms.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    public Integer createStore(StoreDTO storeDTO) {

        if (storeRepository.existsByStoreName(storeDTO.getStoreName())) {
            return -1;
        } else if (storeRepository.existsByStoreManagerPhone(storeDTO.getStoreManagerPhone())) {
            return -2;
        } else if (storeRepository.existsByStoreManagerEmail(storeDTO.getStoreManagerEmail())) {
            return -3;
        }

        Store store = new Store(
                storeDTO.getStoreName(),
                storeDTO.getStoreAddress(),
                storeDTO.getStoreManagerName(),
                storeDTO.getStoreManagerPhone(),
                storeDTO.getStoreManagerEmail());

        storeRepository.save(store);
        return store.getStoreId();
    }

    @Transactional
    public Integer updateStore(Integer storeId, StoreDTO storeDTO) {

        if (storeRepository.existsByStoreNameAndStoreIdNot(storeDTO.getStoreName(), storeId)) {
            return -1;
        } else if (storeRepository.existsByStoreManagerPhoneAndStoreIdNot(storeDTO.getStoreManagerPhone(), storeId)) {
            return -2;
        } else if (storeRepository.existsByStoreManagerEmailAndStoreIdNot(storeDTO.getStoreManagerEmail(), storeId)) {
            return -3;
        }

        Store store = storeRepository.findByStoreId(storeId).orElse(null);

        store.setStoreAddress(storeDTO.getStoreAddress());
        store.setStoreManagerName(storeDTO.getStoreManagerName());
        store.setStoreManagerPhone(storeDTO.getStoreManagerPhone());
        store.setStoreManagerEmail(storeDTO.getStoreManagerEmail());
        store.setStoreUpdatedAt(LocalDateTime.now());

        storeRepository.save(store);
        return store.getStoreId();
    }

    @Transactional
    public boolean deleteStore(Integer storeId) {
        Store store = storeRepository.findByStoreId(storeId).orElse(null);
        if (store == null) {
            return false;
        }

        store.setDeleted(true);
        store.setStoreUpdatedAt(LocalDateTime.now());
        store.setStoreDeletedAt(LocalDateTime.now());
        storeRepository.save(store);
        return true;
    }

    @Transactional
    public boolean restoreStore(Integer storeId) {
        Store store = storeRepository.findByStoreId(storeId).orElse(null);
        if (store == null) {
            return false;
        }

        store.setDeleted(false);
        store.setStoreUpdatedAt(LocalDateTime.now());
        store.setStoreDeletedAt(null);
        storeRepository.save(store);
        return true;
    }
}