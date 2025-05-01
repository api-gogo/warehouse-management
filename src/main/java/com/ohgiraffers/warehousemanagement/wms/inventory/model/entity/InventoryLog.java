package com.ohgiraffers.warehousemanagement.wms.inventory.model.entity;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.InventoryLogTransactionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_logs")
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_log_id")
    private long inventoryLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "quantity_changed", nullable = false)
    private long quantityChanged;

    @Column(name = "inventory_log_content")
    private String inventoryLogContent;

    @Column(name = "user_id")
    private String user_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private InventoryLogTransactionType transactionType;

    @Column(name = "transaction_id")
    private long transactionId;

    @Column(name = "inventory_log_created_at")
    private LocalDateTime inventoryLogCreated;

    @Column(name = "inventory_log_updated_at")
    private LocalDateTime inventoryLogUpdated;

    @Column(name = "inventory_log_deleted_at")
    private LocalDateTime inventoryLogDeleted;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public InventoryLog() {}

    public InventoryLog(long inventoryLogId, Inventory inventory, long quantityChanged, String inventoryLogContent, String user_id, InventoryLogTransactionType transactionType, long transactionId, LocalDateTime inventoryLogCreated, LocalDateTime inventoryLogUpdated, LocalDateTime inventoryLogDeleted, boolean isDeleted) {
        this.inventoryLogId = inventoryLogId;
        this.inventory = inventory;
        this.quantityChanged = quantityChanged;
        this.inventoryLogContent = inventoryLogContent;
        this.user_id = user_id;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.inventoryLogCreated = inventoryLogCreated;
        this.inventoryLogUpdated = inventoryLogUpdated;
        this.inventoryLogDeleted = inventoryLogDeleted;
        this.isDeleted = isDeleted;
    }

    public long getInventoryLogId() {
        return inventoryLogId;
    }

    public void setInventoryLogId(long inventoryLogId) {
        this.inventoryLogId = inventoryLogId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public long getQuantityChanged() {
        return quantityChanged;
    }

    public void setQuantityChanged(long quantityChanged) {
        this.quantityChanged = quantityChanged;
    }

    public String getInventoryLogContent() {
        return inventoryLogContent;
    }

    public void setInventoryLogContent(String inventoryLogContent) {
        this.inventoryLogContent = inventoryLogContent;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public InventoryLogTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(InventoryLogTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getInventoryLogCreated() {
        return inventoryLogCreated;
    }

    public void setInventoryLogCreated(LocalDateTime inventoryLogCreated) {
        this.inventoryLogCreated = inventoryLogCreated;
    }

    public LocalDateTime getInventoryLogUpdated() {
        return inventoryLogUpdated;
    }

    public void setInventoryLogUpdated(LocalDateTime inventoryLogUpdated) {
        this.inventoryLogUpdated = inventoryLogUpdated;
    }

    public LocalDateTime getInventoryLogDeleted() {
        return inventoryLogDeleted;
    }

    public void setInventoryLogDeleted(LocalDateTime inventoryLogDeleted) {
        this.inventoryLogDeleted = inventoryLogDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "InventoryRecord{" +
                "inventoryLogId=" + inventoryLogId +
                ", inventory=" + inventory +
                ", quantityChanged=" + quantityChanged +
                ", inventoryLogContent='" + inventoryLogContent + '\'' +
                ", user_id='" + user_id + '\'' +
                ", transactionType=" + transactionType +
                ", transactionId=" + transactionId +
                ", inventoryLogCreated=" + inventoryLogCreated +
                ", inventoryLogUpdated=" + inventoryLogUpdated +
                ", inventoryLogDeleted=" + inventoryLogDeleted +
                ", isDeleted=" + isDeleted +
                '}';
    }
}