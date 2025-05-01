package com.ohgiraffers.warehousemanagement.wms.sales.service;

import com.ohgiraffers.warehousemanagement.wms.sales.model.dto.SalesDTO;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesItem;

import java.util.List;

public interface SalesService {
    Sales getSalesBySalesId(Integer id); // 수주ID로 수주서 정보 가져올 수 있게
    SalesItem getSalesItemBySalesId(Integer id); // 수주ID로 수주상품목록 정보 가져올 수 있게  

    List<SalesDTO> getAllSales();
}
