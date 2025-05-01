package com.ohgiraffers.warehousemanagement.wms.sales.repository;

import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {
    List<Sales> findAllByOrderBySalesIdDesc(); // 수주서id 기준으로 내림차순 정렬
}
