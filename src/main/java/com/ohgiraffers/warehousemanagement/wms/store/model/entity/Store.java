package com.ohgiraffers.warehousemanagement.wms.store.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name", length = 50, nullable = false, unique = true)
    private String storeName;

    @Column(name = "store_address", length = 100, nullable = false)
    private String storeAddress;

    @Column(name = "store_manager_name", length = 50, nullable = false)
    private String storeManagerName;

    @Column(name = "store_manager_phone", length = 50, nullable = false, unique = true)
    private String storeManagerPhone;

    @Column(name = "store_manager_email", length = 100, nullable = false, unique = true)
    private String storeManagerEmail;

    @Column(name = "store_created_at", nullable = false)
    private LocalDateTime storeCreatedAt;

    @Column(name = "store_updated_at")
    private LocalDateTime storeUpdatedAt;

    @Column(name = "store_deleted_at")
    private LocalDateTime storeDeletedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    protected Store() {}

    public Store(String storeName, String storeAddress, String storeManagerName, String storeManagerPhone, String storeManagerEmail) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeManagerName = storeManagerName;
        this.storeManagerPhone = storeManagerPhone;
        this.storeManagerEmail = storeManagerEmail;
        this.storeCreatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getStoreManagerName() {
        return storeManagerName;
    }

    public String getStoreManagerPhone() {
        return storeManagerPhone;
    }

    public String getStoreManagerEmail() {
        return storeManagerEmail;
    }

    public LocalDateTime getStoreCreatedAt() {
        return storeCreatedAt;
    }

    public LocalDateTime getStoreUpdatedAt() {
        return storeUpdatedAt;
    }

    public LocalDateTime getStoreDeletedAt() {
        return storeDeletedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public void setStoreManagerName(String storeManagerName) {
        this.storeManagerName = storeManagerName;
    }

    public void setStoreManagerPhone(String storeManagerPhone) {
        this.storeManagerPhone = storeManagerPhone;
    }

    public void setStoreManagerEmail(String storeManagerEmail) {
        this.storeManagerEmail = storeManagerEmail;
    }

    public void setStoreUpdatedAt(LocalDateTime storeUpdatedAt) {
        this.storeUpdatedAt = storeUpdatedAt;
    }

    public void setStoreDeletedAt(LocalDateTime storeDeletedAt) {
        this.storeDeletedAt = storeDeletedAt;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeManagerName='" + storeManagerName + '\'' +
                ", storeManagerPhone='" + storeManagerPhone + '\'' +
                ", storeManagerEmail='" + storeManagerEmail + '\'' +
                ", storeCreatedAt=" + storeCreatedAt +
                ", storeUpdatedAt=" + storeUpdatedAt +
                ", storeDeletedAt=" + storeDeletedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
