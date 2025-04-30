package com.ohgiraffers.warehousemanagement.wms.store.service;

import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;

public interface StoreService {

    StoreDTO findById(Integer storeId);
    StoreDTO findByName(String storeName);
}