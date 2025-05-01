package com.ohgiraffers.warehousemanagement.wms.sales.repository;

import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesItemsRepository extends JpaRepository<SalesItem, Integer> {
    void deleteBySalesId(Sales sales);

    List<SalesItem> findBySalesId(Sales salesId);
}
