package com.ohgiraffers.warehousemanagement.wms.supplier.service;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;

public interface SupplierService {

    SupplierDTO findById(Integer supplierId);
    SupplierDTO findByName(String supplierName);
}