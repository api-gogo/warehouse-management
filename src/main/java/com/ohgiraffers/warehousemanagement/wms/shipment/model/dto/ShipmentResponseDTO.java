package com.ohgiraffers.warehousemanagement.wms.shipment.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ShipmentResponseDTO {

    private Integer shipmentId;
    private Integer saleId;
    private Integer userId;
    private LocalDateTime shipmentDate;
    private String shipmentStatus;
    private String shipmentReason;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    
    // 출고 항목 정보 (로트 번호, 수량 등)
    private List<ShipmentItemDTO> items;

    public ShipmentResponseDTO() {}

    public ShipmentResponseDTO(Integer shipmentId, Integer saleId, Integer userId, LocalDateTime shipmentDate,
                                String shipmentStatus, String shipmentReason, LocalDateTime updatedAt,
                                LocalDateTime createdAt) {
        this.shipmentId = shipmentId;
        this.saleId = saleId;
        this.userId = userId;
        this.shipmentDate = shipmentDate;
        this.shipmentStatus = shipmentStatus;
        this.shipmentReason = shipmentReason;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(LocalDateTime shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getShipmentReason() {
        return shipmentReason;
    }

    public void setShipmentReason(String shipmentReason) {
        this.shipmentReason = shipmentReason;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<ShipmentItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ShipmentItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ShipmentResponseDTO{" +
                "shipmentId=" + shipmentId +
                ", saleId=" + saleId +
                ", userId=" + userId +
                ", shipmentDate=" + shipmentDate +
                ", shipmentStatus='" + shipmentStatus + '\'' +
                ", shipmentReason='" + shipmentReason + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", items=" + items +
                '}';
    }
}