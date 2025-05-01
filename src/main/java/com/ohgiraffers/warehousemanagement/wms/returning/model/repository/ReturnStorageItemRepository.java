package com.ohgiraffers.warehousemanagement.wms.returning.model.repository;

import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnStorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnStorageItemRepository extends JpaRepository<ReturnStorageItem,Integer> {
}
