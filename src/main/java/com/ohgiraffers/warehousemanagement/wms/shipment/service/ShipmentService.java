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

    Integer getSaleIdByShipmentId(Integer shipmentId);
}

/*
* //출고ID 를 받아서 수주ID를 반환하는 메서드
* @Autowired
* private ShipmentService shipmentService;
*
* public void someMethodInReturns(Integer shipmentId) {
* Integer saleId = shipmentService.getSaleIdByShipmentId(shipmentId);
*
*  // saleId를 사용한 추가 로직
* }
*
* //shipmentId가 유효하지 않거나 출고가 존재하지 않으면 RuntimeException이 발생하니, 타 도메인에서 적절히 예외 처리 필요.
* //SecurityConfig 에서 /shipments/** 경로에 접근하려면 출고_사원, 출고_매니저, 출고_관리자 권한 필요한데 이건 태빈이형한테 요청 해봐야할듯
* -> SecurityConfig 권한 태빈이형한테 수정 부탁 드려보세용
 **/
