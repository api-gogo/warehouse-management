package com.ohgiraffers.warehousemanagement.wms.shipment.model.dto;

/**
 * 출고 항목 정보를 담는 DTO 클래스
 */
public class ShipmentItemDTO {
    
    private Integer shipmentItemId;
    private Integer shipmentId;
    private Integer productId;
    private String productName;
    private String lotNumber;
    private Integer quantity;
    
    public ShipmentItemDTO() {
    }
    
    public ShipmentItemDTO(Integer shipmentItemId, Integer shipmentId, Integer productId, String productName, String lotNumber, Integer quantity) {
        this.shipmentItemId = shipmentItemId;
        this.shipmentId = shipmentId;
        this.productId = productId;
        this.productName = productName;
        this.lotNumber = lotNumber;
        this.quantity = quantity;
    }
    
    public Integer getShipmentItemId() {
        return shipmentItemId;
    }
    
    public void setShipmentItemId(Integer shipmentItemId) {
        this.shipmentItemId = shipmentItemId;
    }
    
    public Integer getShipmentId() {
        return shipmentId;
    }
    
    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }
    
    public Integer getProductId() {
        return productId;
    }
    
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getLotNumber() {
        return lotNumber;
    }
    
    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "ShipmentItemDTO{" +
                "shipmentItemId=" + shipmentItemId +
                ", shipmentId=" + shipmentId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", lotNumber='" + lotNumber + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
