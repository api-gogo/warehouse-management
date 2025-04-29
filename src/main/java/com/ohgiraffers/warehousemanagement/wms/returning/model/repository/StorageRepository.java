package com.ohgiraffers.warehousemanagement.wms.returning.model.repository;

import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.Storages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storages,Integer> {
}
