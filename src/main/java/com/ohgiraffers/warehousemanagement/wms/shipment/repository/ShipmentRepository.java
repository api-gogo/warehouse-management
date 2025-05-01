package com.ohgiraffers.warehousemanagement.wms.shipment.repository;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 출고 데이터를 관리하는 JPA 리포지토리
 */
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    /**
     * shipments 테이블에서 고유한 sale_id 목록을 조회
     * @return 수주 ID 목록
     */
    @Query("SELECT DISTINCT s.saleId FROM Shipment s")
    List<Integer> findAllSaleIds();
}