package com.ohgiraffers.warehousemanagement.wms.store.model.dto;

import java.time.LocalDateTime;

public class StoreDTO {

    private Integer storeId;
    private String storeName;
    private String storeAddress;
    private String storeManagerName;
    private String storeManagerPhone;
    private String storeManagerEmail;
    private LocalDateTime storeCreatedAt;
    private LocalDateTime storeUpdatedAt;
    private LocalDateTime storeDeletedAt;
    private boolean isDeleted;

    public StoreDTO() {}

    public StoreDTO(Integer storeId, String storeName, String storeAddress, String storeManagerName, String storeManagerPhone, String storeManagerEmail, LocalDateTime storeCreatedAt, LocalDateTime storeUpdatedAt, LocalDateTime storeDeletedAt, boolean isDeleted) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeManagerName = storeManagerName;
        this.storeManagerPhone = storeManagerPhone;
        this.storeManagerEmail = storeManagerEmail;
        this.storeCreatedAt = storeCreatedAt;
        this.storeUpdatedAt = storeUpdatedAt;
        this.storeDeletedAt = storeDeletedAt;
        this.isDeleted = isDeleted;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreManagerName() {
        return storeManagerName;
    }

    public void setStoreManagerName(String storeManagerName) {
        this.storeManagerName = storeManagerName;
    }

    public String getStoreManagerPhone() {
        return storeManagerPhone;
    }

    public void setStoreManagerPhone(String storeManagerPhone) {
        this.storeManagerPhone = storeManagerPhone;
    }

    public String getStoreManagerEmail() {
        return storeManagerEmail;
    }

    public void setStoreManagerEmail(String storeManagerEmail) {
        this.storeManagerEmail = storeManagerEmail;
    }

    public LocalDateTime getStoreCreatedAt() {
        return storeCreatedAt;
    }

    public void setStoreCreatedAt(LocalDateTime storeCreatedAt) {
        this.storeCreatedAt = storeCreatedAt;
    }

    public LocalDateTime getStoreUpdatedAt() {
        return storeUpdatedAt;
    }

    public void setStoreUpdatedAt(LocalDateTime storeUpdatedAt) {
        this.storeUpdatedAt = storeUpdatedAt;
    }

    public LocalDateTime getStoreDeletedAt() {
        return storeDeletedAt;
    }

    public void setStoreDeletedAt(LocalDateTime storeDeletedAt) {
        this.storeDeletedAt = storeDeletedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
