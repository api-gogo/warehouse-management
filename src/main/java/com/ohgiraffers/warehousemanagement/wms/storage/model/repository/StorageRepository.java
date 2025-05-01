package com.ohgiraffers.warehousemanagement.wms.storage.model.repository;

// DB에 저장하거나 조회하는 역할

import com.ohgiraffers.warehousemanagement.wms.category.model.entity.Category;
import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import com.ohgiraffers.warehousemanagement.wms.storage.model.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageRepository extends JpaRepository<Storage, Integer> {
    List<Storage> findByStorageStatus(StorageStatus storageStatus);
}


