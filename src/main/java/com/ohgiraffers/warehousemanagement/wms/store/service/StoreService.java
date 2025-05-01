package com.ohgiraffers.warehousemanagement.wms.store.service;

import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;

import java.util.List;

public interface StoreService {

    StoreDTO findById(Integer storeId);
    StoreDTO findByName(String storeName);
    List<StoreDTO> searchByNameContainingAndIsDeletedFalse(String storeName);
}