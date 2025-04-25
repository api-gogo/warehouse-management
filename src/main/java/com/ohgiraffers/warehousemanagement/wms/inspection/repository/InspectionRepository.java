package com.ohgiraffers.warehousemanagement.wms.inspection.repository;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.InspectionTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InspectionRepository extends JpaRepository<Inspection, Integer> {
    Optional<Inspection> findByTransactionTypeAndTransactionId(InspectionTransactionType transactionType, Integer transactionId);

    List<Inspection> findAllByTransactionType(InspectionTransactionType transactionType);
}
