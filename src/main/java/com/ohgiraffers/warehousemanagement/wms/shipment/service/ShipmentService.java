package com.ohgiraffers.warehousemanagement.wms.shipment.service;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipment;

public interface ShipmentService {

    ShipmentPageResponseDTO getAllShipments(int page, int size, String status, String searchTerm);

    ShipmentResponseDTO getShipmentById(Integer shipmentId);

    Shipment findShipmentById(Integer shipmentId);

    ShipmentResponseDTO createShipment(ShipmentCreateDTO createDTO);

    ShipmentResponseDTO updateShipment(Integer shipmentId, ShipmentCreateDTO updateDTO);

    void cancelShipment(Integer shipmentId);
}