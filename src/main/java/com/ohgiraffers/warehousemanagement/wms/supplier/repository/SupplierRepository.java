package com.ohgiraffers.warehousemanagement.wms.supplier.repository;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    Optional<Supplier> findBySupplierId(Integer supplierId);
    boolean existsBySupplierId(Integer supplierId);


}
