package com.ohgiraffers.warehousemanagement.wms.store.service;

import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {

    StoreDTO findById(Integer storeId);
    StoreDTO findByName(String storeName);
}