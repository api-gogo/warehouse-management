package com.ohgiraffers.warehousemanagement.wms.inspection.repository;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    Optional<Inspection> findByTransactionTypeAndTransactionId(InspectionTransactionType transactionType, Long transactionId);

    Page<Inspection> findAllByOrderByInspectionIdDesc(Pageable pageable);

    Page<Inspection> findAllByTransactionTypeOrderByInspectionIdDesc(InspectionTransactionType transactionType, Pageable pageable);

//    @Query("SELECT i FROM Inspection i WHERE " +
//            "(:searchType = 'inspectionId' AND i.inspectionId LIKE %:search%) OR " +
//            "(:searchType = 'userId' AND i.userId LIKE %:search%) OR " +
//            "(:searchType = 'transactionId' AND i.transactionId LIKE %:search%) OR " +
//            "(:searchType = 'inspectionDate' AND i.inspectionDate LIKE :search)")
//    Page<Inspection> findAllBySearchOrderByInspectionIdDesc(String searchType, Long search, Pageable pageable);
}
