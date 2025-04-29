package com.ohgiraffers.warehousemanagement.wms.store.repository;

import com.ohgiraffers.warehousemanagement.wms.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {

    Optional<Store> findByStoreId(Integer storeId);
    boolean existsByStoreId(Integer storeId);


}
