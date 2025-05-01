package com.ohgiraffers.warehousemanagement.wms.shipment.service;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipment;

import java.util.List;

/*
 * 출고 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 */
public interface ShipmentService {
    /*
     * 모든 출고 목록을 페이지 단위로 조회 (검색 및 필터링 포함)
     */
    ShipmentPageResponseDTO getAllShipments(int page, int size, String status, String searchTerm);

    /*
     * 특정 출고 ID로 출고 상세 정보 조회
     */
    ShipmentResponseDTO getShipmentById(Integer shipmentId);

    /*
     * 특정 출고 ID로 출고 엔티티 조회
     */
    Shipment findShipmentById(Integer shipmentId);

    /*
     * 새로운 출고 생성
     */
    ShipmentResponseDTO createShipment(ShipmentCreateDTO createDTO);

    /*
     * 특정 출고 ID로 출고 정보 수정
     */
    ShipmentResponseDTO updateShipment(Integer shipmentId, ShipmentCreateDTO updateDTO);

    /*
     * 특정 출고 ID로 출고 취소
     */
    void cancelShipment(Integer shipmentId);

    /*
     * 특정 출고 ID로 연관된 수주 ID 조회
     */
    Integer getSaleIdByShipmentId(Integer shipmentId);

    /*
     * 모든 수주 ID 목록 조회
     */
    List<Integer> getAllSaleIds();
}