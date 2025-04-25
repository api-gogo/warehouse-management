package com.ohgiraffers.warehousemanagement.wms.shipments.model.repository;

import com.ohgiraffers.warehousemanagement.wms.shipments.model.entity.Shipments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentsRepository extends JpaRepository<Shipments, Long> {
}
