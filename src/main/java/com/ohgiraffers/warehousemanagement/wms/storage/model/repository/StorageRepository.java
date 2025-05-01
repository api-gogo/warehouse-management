package com.ohgiraffers.warehousemanagement.wms.storage.model.repository;

import com.ohgiraffers.warehousemanagement.wms.storage.model.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Integer> {


    List<Storage> findByPurchase_PurchaseId(Integer purchaseId);


    boolean existsByPurchase_PurchaseId(Integer purchaseId);
}
