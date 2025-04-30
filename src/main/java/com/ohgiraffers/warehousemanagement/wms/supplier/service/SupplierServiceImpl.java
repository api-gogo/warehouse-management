package com.ohgiraffers.warehousemanagement.wms.supplier.service;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;
import com.ohgiraffers.warehousemanagement.wms.supplier.model.entity.Supplier;
import com.ohgiraffers.warehousemanagement.wms.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<SupplierDTO> findAll(String search, String status, Pageable pageable) {
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

    @Override
    public SupplierDTO findById(Integer supplierId) {
        Optional<Supplier> supplier = supplierRepository.findBySupplierId(supplierId);

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

    @Override
    public SupplierDTO findByName(String supplierName) {
        Optional<Supplier> supplier = supplierRepository.findBySupplierName(supplierName);

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

    public boolean updateSupplier(Integer supplierId, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findBySupplierId(supplierId).orElse(null);
        if (supplier == null) {
            return false;
        }

        supplier.setSupplierAddress(supplierDTO.getSupplierAddress());
        supplier.setSupplierManagerName(supplierDTO.getSupplierManagerName());
        supplier.setSupplierManagerPhone(supplierDTO.getSupplierManagerPhone());
        supplier.setSupplierManagerEmail(supplierDTO.getSupplierManagerEmail());
        supplier.setSupplierUpdatedAt(LocalDateTime.now());

        supplierRepository.save(supplier);
        return true;
    }

    public boolean deleteSupplier(Integer supplierId) {
        Supplier supplier = supplierRepository.findBySupplierId(supplierId).orElse(null);
        if (supplier == null) {
            return false;
        }

        supplier.setDeleted(true);
        supplier.setSupplierUpdatedAt(LocalDateTime.now());
        supplier.setSupplierDeletedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
        return true;
    }
}