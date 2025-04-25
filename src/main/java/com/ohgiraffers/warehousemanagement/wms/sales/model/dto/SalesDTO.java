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
    private LocalDateTime salesUpdatedAt;

    public SalesDTO() {
    }

    public SalesDTO(Integer salesId, Integer storeId, Integer userId, LocalDate salesDate, LocalDate shippingDueDate, SalesStatus salesStatus, LocalDateTime salesCreatedAt, LocalDateTime salesUpdatedAt) {
        this.salesId = salesId;
        this.storeId = storeId;
        this.userId = userId;
        this.salesDate = salesDate;
        this.shippingDueDate = shippingDueDate;
        this.salesStatus = salesStatus;
        this.salesCreatedAt = salesCreatedAt;
        this.salesUpdatedAt = salesUpdatedAt;
    }

    public Integer getSalesId() {
        return salesId;
    }

    public void setSalesId(Integer salesId) {
        this.salesId = salesId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public LocalDate getShippingDueDate() {
        return shippingDueDate;
    }

    public void setShippingDueDate(LocalDate shippingDueDate) {
        this.shippingDueDate = shippingDueDate;
    }

    public SalesStatus getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(SalesStatus salesStatus) {
        this.salesStatus = salesStatus;
    }

    public LocalDateTime getSalesCreatedAt() {
        return salesCreatedAt;
    }

    public void setSalesCreatedAt(LocalDateTime salesCreatedAt) {
        this.salesCreatedAt = salesCreatedAt;
    }

    public LocalDateTime getSalesUpdatedAt() {
        return salesUpdatedAt;
    }

    public void setSalesUpdatedAt(LocalDateTime salesUpdatedAt) {
        this.salesUpdatedAt = salesUpdatedAt;
    }

    @Override
    public String toString() {
        return "SalesDTO{" +
                "salesId=" + salesId +
                ", storeId=" + storeId +
                ", userId=" + userId +
                ", salesDate=" + salesDate +
                ", shippingDueDate=" + shippingDueDate +
                ", salesStatus=" + salesStatus +
                ", salesCreatedAt=" + salesCreatedAt +
                ", salesUpdatedAt=" + salesUpdatedAt +
                '}';
    }
}
