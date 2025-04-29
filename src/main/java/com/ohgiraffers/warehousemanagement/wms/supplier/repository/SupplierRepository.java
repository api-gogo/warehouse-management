package com.ohgiraffers.warehousemanagement.wms.supplier.repository;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    Optional<Supplier> findBySupplierId(Integer supplierId);
    boolean existsBySupplierId(Integer supplierId);

    @Query("SELECT s FROM Supplier s " +
            "WHERE (s.isDeleted = :status) " +
            "AND (:search IS NULL OR " +
            "     lower(s.supplierName) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.supplierAddress) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.supplierManagerName) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.supplierManagerEmail) LIKE lower(concat('%', :search, '%')) OR " +
            "     lower(s.supplierManagerPhone) LIKE lower(concat('%', :search, '%')))")
    Page<Supplier> findByStatusAndSearch(@Param("status") boolean status,
                                     @Param("search") String search,
                                     Pageable pageable);
}
