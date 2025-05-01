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

    @Query("SELECT i FROM Inspection i WHERE " +
            "(:searchType = 'inspectionId' AND STR(i.inspectionId) = :search) OR " +
            "(:searchType = 'userId' AND STR(i.user.userId) = :search) OR " +
            "(:searchType = 'userName' AND STR(i.user.userName) LIKE CONCAT('%', :search, '%')) OR " +
            "(:searchType = 'transactionId' AND STR(i.transactionId) = :search) OR " +
            "(:searchType = 'inspectionDate' AND STR(i.inspectionDate) LIKE CONCAT('%', :search, '%')) " +
            "ORDER BY i.inspectionId DESC")
    Page<Inspection> findAllBySearchOrderByInspectionIdDesc(String searchType, String search, Pageable pageable);

    @Query("SELECT i FROM Inspection i WHERE i.transactionType = :inspectionTransactionType AND (" +
            "(:searchType = 'inspectionId' AND STR(i.inspectionId) = :search) OR " +
            "(:searchType = 'userId' AND STR(i.user.userId) = :search) OR " +
            "(:searchType = 'userName' AND STR(i.user.userName) LIKE CONCAT('%', :search, '%')) OR " +
            "(:searchType = 'transactionId' AND STR(i.transactionId) = :search) OR " +
            "(:searchType = 'inspectionDate' AND STR(i.inspectionDate) LIKE CONCAT('%', :search, '%'))" +
            ") ORDER BY i.inspectionId DESC")
    Page<Inspection> findAllByTransactionTypeAndSearchOrderByInspectionIdDesc(InspectionTransactionType inspectionTransactionType, String searchType, String search, Pageable pageable);
}
