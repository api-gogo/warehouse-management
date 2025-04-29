package com.ohgiraffers.warehousemanagement.wms.supplier.service;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;
import com.ohgiraffers.warehousemanagement.wms.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<SupplierDTO> getSuppliers(String search, String status, Pageable pageable) {
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

    public SupplierDTO getSupplierBySupplierId(Integer supplierId) {
        Optional<Supplier> supplier = supplierRepository.findById(supplierId);

        return supplier.map(s -> new SupplierDTO(
                s.getSupplierId(),
                s.getSupplierName(),
                s.getSupplierAddress(),
                s.getSupplierManagerName(),
                s.getSupplierManagerPhone(),
                s.getSupplierManagerEmail(),
                s.getSupplierCreatedAt(),
                s.getSupplierUpdatedAt(),
                s.getSupplierDeletedAt(),
                s.getDeleted()
        )).orElse(null);
    }

    public Integer createSupplier(SupplierDTO supplierDTO) {

        if (supplierRepository.existsBySupplierName(supplierDTO.getSupplierName())) {
            return -1;
        } else if (supplierRepository.existsBySupplierManagerPhone(supplierDTO.getSupplierManagerPhone())) {
            return -2;
        } else if (supplierRepository.existsBySupplierManagerEmail(supplierDTO.getSupplierManagerEmail())) {
            return -3;
        }

        try {
            Supplier supplier = new Supplier(
                    supplierDTO.getSupplierName(),
                    supplierDTO.getSupplierAddress(),
                    supplierDTO.getSupplierManagerName(),
                    supplierDTO.getSupplierManagerPhone(),
                    supplierDTO.getSupplierManagerEmail()
            );

            supplierRepository.save(supplier);
            return supplier.getSupplierId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
