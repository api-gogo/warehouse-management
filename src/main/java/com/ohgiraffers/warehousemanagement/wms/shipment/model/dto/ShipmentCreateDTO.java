package com.ohgiraffers.warehousemanagement.wms.shipment.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ShipmentCreateDTO {

    @NotNull(message = "수주 ID는 필수 입력 항목입니다.")
    private Integer saleId;

    @NotNull(message = "출고 담당자 ID는 필수 입력 항목입니다.")
    private Integer userId;

    @NotNull(message = "출고일은 필수 입력 항목입니다.")
    private LocalDateTime shipmentDate;

    @NotNull(message = "출고 상태는 필수 입력 항목입니다.")
    @Size(max = 20, message = "출고 상태는 20자를 초과할 수 없습니다.")
    private String shipmentStatus;

    @Size(max = 255, message = "출고 사유는 255자를 초과할 수 없습니다.")
    private String shipmentReason;

    public ShipmentCreateDTO() {}

    public ShipmentCreateDTO(Integer saleId, Integer userId, LocalDateTime shipmentDate, String shipmentStatus, String shipmentReason) {
        this.saleId = saleId;
        this.userId = userId;
        this.shipmentDate = shipmentDate;
        this.shipmentStatus = shipmentStatus;
        this.shipmentReason = shipmentReason;
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

    @Override
    public String toString() {
        return "ShipmentCreateDTO{" +
                "saleId=" + saleId +
                ", userId=" + userId +
                ", shipmentDate=" + shipmentDate +
                ", shipmentStatus='" + shipmentStatus + '\'' +
                ", shipmentReason='" + shipmentReason + '\'' +
                '}';
    }
}