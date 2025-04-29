package com.ohgiraffers.warehousemanagement.wms.supplier.service;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;
import com.ohgiraffers.warehousemanagement.wms.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<SupplierDTO> findSuppliers(String search, String status, Pageable pageable) {
        boolean isDeleted = false;

        if (status != null) {
            if (status.equals("deleted")) {
                isDeleted = true;
            }
        }

        Page<Supplier> supplierPage = supplierRepository.findByStatusAndSearch(isDeleted, search, pageable);

        return supplierPage.map(supplier -> new SupplierDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierAddress(),
                supplier.getSupplierManagerName(),
                supplier.getSupplierManagerPhone(),
                supplier.getSupplierManagerEmail(),
                supplier.getSupplierCreatedAt(),
                supplier.getSupplierUpdatedAt(),
                supplier.getSupplierDeletedAt(),
                supplier.getDeleted()
        ));
    }
}
