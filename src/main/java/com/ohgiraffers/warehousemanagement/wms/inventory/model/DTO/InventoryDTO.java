package com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryDTO {
    private Long inventoryId;
    private Long storageId;
    private Long productId;
    private String lotNumber;
    private Long locationCode;
    private Long availableStock;
    private Long allocatedStock;
    private Long disposedStock;
    private LocalDate inventoryExpiryDate;
    private LocalDateTime inventoryCreatedAt;
    private LocalDateTime inventoryUpdatedAt;

    public InventoryDTO() {}

    public InventoryDTO(Long inventoryId, Long storageId, Long productId, String lotNumber, Long locationCode, Long availableStock, Long allocatedStock, Long disposedStock, LocalDate inventoryExpiryDate, LocalDateTime inventoryCreatedAt, LocalDateTime inventoryUpdatedAt) {
        this.inventoryId = inventoryId;
        this.storageId = storageId;
        this.productId = productId;
        this.lotNumber = lotNumber;
        this.locationCode = locationCode;
        this.availableStock = availableStock;
        this.allocatedStock = allocatedStock;
        this.disposedStock = disposedStock;
        this.inventoryExpiryDate = inventoryExpiryDate;
        this.inventoryCreatedAt = inventoryCreatedAt;
        this.inventoryUpdatedAt = inventoryUpdatedAt;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Long getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Long locationCode) {
        this.locationCode = locationCode;
    }

    public Long getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Long availableStock) {
        this.availableStock = availableStock;
    }

    public Long getAllocatedStock() {
        return allocatedStock;
    }

    public void setAllocatedStock(Long allocatedStock) {
        this.allocatedStock = allocatedStock;
    }

    public Long getDisposedStock() {
        return disposedStock;
    }

    public void setDisposedStock(Long dispsosedStock) {
        this.disposedStock = dispsosedStock;
    }

    public LocalDate getInventoryExpiryDate() {
        return inventoryExpiryDate;
    }

    public void setInventoryExpiryDate(LocalDate inventoryExpiryDate) {
        this.inventoryExpiryDate = inventoryExpiryDate;
    }

    public LocalDateTime getInventoryCreatedAt() {
        return inventoryCreatedAt;
    }

    public void setInventoryCreatedAt(LocalDateTime inventoryCreatedAt) {
        this.inventoryCreatedAt = inventoryCreatedAt;
    }

    public LocalDateTime getInventoryUpdatedAt() {
        return inventoryUpdatedAt;
    }

    public void setInventoryUpdatedAt(LocalDateTime inventoryUpdatedAt) {
        this.inventoryUpdatedAt = inventoryUpdatedAt;
    }

    @Override
    public String toString() {
        return "InventoryDTO{" +
                "inventoryId=" + inventoryId +
                ", storageId=" + storageId +
                ", productId=" + productId +
                ", lotNumber='" + lotNumber + '\'' +
                ", locationCode=" + locationCode +
                ", availableStock=" + availableStock +
                ", allocatedStock=" + allocatedStock +
                ", dispsosedStock=" + disposedStock +
                ", inventoryExpiryDate=" + inventoryExpiryDate +
                ", inventoryCreatedAt=" + inventoryCreatedAt +
                ", inventoryUpdatedAt=" + inventoryUpdatedAt +
                '}';
    }
}
