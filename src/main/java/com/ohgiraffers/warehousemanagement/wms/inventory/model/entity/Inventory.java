package com.ohgiraffers.warehousemanagement.wms.inventory.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private long inventoryid;

    @Column(name = "storage_id")
    private long storageId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "location_code")
    private long locationCode;

    @Column(name = "available_stock")
    private long availableStock;

    @Column(name = "allocated_stock")
    private long allocatedStock;

    @Column(name = "disposed_stock")
    private long disposedStock;

    @Column(name = "inventory_expiry_date")
    private LocalDateTime inventoryExpiryDate;

    @Column(name = "inventory_created_at")
    private LocalDateTime inventoryCreatedAt;

    @Column(name = "inventory_updated_at")
    private LocalDateTime inventoryUpdatedAt;

    public Inventory() {}

    public Inventory(long inventoryid, long storageId, long productId, String lotNumber, long locationCode, long availableStock, long allocatedStock, long disposedStock, LocalDateTime inventoryExpiryDate, LocalDateTime inventoryCreatedAt, LocalDateTime inventoryUpdatedAt) {
        this.inventoryid = inventoryid;
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

    public long getInventoryid() {
        return inventoryid;
    }

    public void setInventoryid(long inventoryid) {
        this.inventoryid = inventoryid;
    }

    public long getStorageId() {
        return storageId;
    }

    public void setStorageId(long storageId) {
        this.storageId = storageId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public long getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(long locationCode) {
        this.locationCode = locationCode;
    }

    public long getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(long availableStock) {
        this.availableStock = availableStock;
    }

    public long getAllocatedStock() {
        return allocatedStock;
    }

    public void setAllocatedStock(long allocatedStock) {
        this.allocatedStock = allocatedStock;
    }

    public long getDisposedStock() {
        return disposedStock;
    }

    public void setDisposedStock(long disposedStock) {
        this.disposedStock = disposedStock;
    }

    public LocalDateTime getInventoryExpiryDate() {
        return inventoryExpiryDate;
    }

    public void setInventoryExpiryDate(LocalDateTime inventoryExpiryDate) {
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
        return "Inventory{" +
                "inventoryid=" + inventoryid +
                ", storageId=" + storageId +
                ", productId=" + productId +
                ", lotNumber='" + lotNumber + '\'' +
                ", locationCode=" + locationCode +
                ", availableStock=" + availableStock +
                ", allocatedStock=" + allocatedStock +
                ", disposedStock=" + disposedStock +
                ", inventoryExpiryDate=" + inventoryExpiryDate +
                ", inventoryCreatedAt=" + inventoryCreatedAt +
                ", inventoryUpdatedAt=" + inventoryUpdatedAt +
                '}';
    }
}

