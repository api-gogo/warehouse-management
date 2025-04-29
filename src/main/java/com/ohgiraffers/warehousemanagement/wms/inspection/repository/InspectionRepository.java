package com.ohgiraffers.warehousemanagement.wms.inspection.repository;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    Optional<Inspection> findByTransactionTypeAndTransactionId(InspectionTransactionType transactionType, Integer transactionId);

    Page<Inspection> findAllByOrderByInspectionIdDesc(Pageable pageable);

    Page<Inspection> findAllByTransactionTypeOrderByInspectionIdDesc(InspectionTransactionType transactionType, Pageable pageable);
}
