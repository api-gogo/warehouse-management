package com.ohgiraffers.warehousemanagement.wms.returning.model.repository;

import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnShipmentRepository extends JpaRepository<ReturnShipment, Integer> {
    List<ReturnShipment> findAllByIsDeleted(boolean isDeleted);
}
