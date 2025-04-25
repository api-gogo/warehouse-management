package com.ohgiraffers.warehousemanagement.wms.sales.repository;

import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {
}
