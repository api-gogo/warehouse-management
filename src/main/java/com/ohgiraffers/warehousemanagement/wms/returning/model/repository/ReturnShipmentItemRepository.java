package com.ohgiraffers.warehousemanagement.wms.returning.model.repository;

import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnShipmentItemRepository extends JpaRepository<ReturnShipmentItem, Integer> {
}
