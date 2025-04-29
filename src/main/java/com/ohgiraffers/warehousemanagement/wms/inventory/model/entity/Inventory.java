package com.ohgiraffers.warehousemanagement.wms.inventory.model.entity;

import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private long inventoryId;

    @Column(name = "storage_id")
    private long storageId;

    // 재고 테이블의 product_id은 상품 테이블의 기본키 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

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
    private LocalDate inventoryExpiryDate;

    @Column(name = "inventory_created_at")
    private LocalDateTime inventoryCreatedAt;

    @Column(name = "inventory_updated_at")
    private LocalDateTime inventoryUpdatedAt;

    public Inventory() {}


    public Inventory(long inventoryId, long storageId, Product product, String lotNumber, long locationCode, long availableStock, long allocatedStock, long disposedStock, LocalDate inventoryExpiryDate, LocalDateTime inventoryCreatedAt, LocalDateTime inventoryUpdatedAt) {
        this.inventoryId = inventoryId;
        this.storageId = storageId;
        this.product = product;
        this.lotNumber = lotNumber;
        this.locationCode = locationCode;
        this.availableStock = availableStock;
        this.allocatedStock = allocatedStock;
        this.disposedStock = disposedStock;
        this.inventoryExpiryDate = inventoryExpiryDate;
        this.inventoryCreatedAt = inventoryCreatedAt;
        this.inventoryUpdatedAt = inventoryUpdatedAt;
    }

    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public long getStorageId() {
        return storageId;
    }

    public void setStorageId(long storageId) {
        this.storageId = storageId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", storageId=" + storageId +
                ", product=" + product +
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
