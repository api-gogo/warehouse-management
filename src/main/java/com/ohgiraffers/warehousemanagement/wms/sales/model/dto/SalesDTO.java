package com.ohgiraffers.warehousemanagement.wms.sales.model.dto;

import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SalesDTO {
    private Integer salesId;
    private Integer storeId;
    private Integer userId;
    private LocalDate salesDate;
    private LocalDate shippingDueDate;
    private SalesStatus salesStatus;
    private LocalDateTime salesCreatedAt;
}
