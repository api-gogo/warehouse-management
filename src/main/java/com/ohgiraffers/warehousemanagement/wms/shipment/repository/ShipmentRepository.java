package com.ohgiraffers.warehousemanagement.wms.shipment.repository;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
}
