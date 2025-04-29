package com.ohgiraffers.warehousemanagement.wms.supplier.model.dto;

import java.time.LocalDateTime;

public class SupplierDTO {

    private Integer supplierId;
    private String supplierName;
    private String supplierAddress;
    private String supplierManagerName;
    private String supplierManagerPhone;
    private String supplierManagerEmail;
    private LocalDateTime supplierCreatedAt;
    private LocalDateTime supplierUpdatedAt;
    private LocalDateTime supplierDeletedAt;
    private boolean isDeleted;

    public SupplierDTO() {}

    public SupplierDTO(Integer supplierId, String supplierName, String supplierAddress, String supplierManagerName, String supplierManagerPhone, String supplierManagerEmail, LocalDateTime supplierCreatedAt, LocalDateTime supplierUpdatedAt, LocalDateTime supplierDeletedAt, boolean isDeleted) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.supplierManagerName = supplierManagerName;
        this.supplierManagerPhone = supplierManagerPhone;
        this.supplierManagerEmail = supplierManagerEmail;
        this.supplierCreatedAt = supplierCreatedAt;
        this.supplierUpdatedAt = supplierUpdatedAt;
        this.supplierDeletedAt = supplierDeletedAt;
        this.isDeleted = isDeleted;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierManagerName() {
        return supplierManagerName;
    }

    public void setSupplierManagerName(String supplierManagerName) {
        this.supplierManagerName = supplierManagerName;
    }

    public String getSupplierManagerPhone() {
        return supplierManagerPhone;
    }

    public void setSupplierManagerPhone(String supplierManagerPhone) {
        this.supplierManagerPhone = supplierManagerPhone;
    }

    public String getSupplierManagerEmail() {
        return supplierManagerEmail;
    }

    public void setSupplierManagerEmail(String supplierManagerEmail) {
        this.supplierManagerEmail = supplierManagerEmail;
    }

    public LocalDateTime getSupplierCreatedAt() {
        return supplierCreatedAt;
    }

    public void setSupplierCreatedAt(LocalDateTime supplierCreatedAt) {
        this.supplierCreatedAt = supplierCreatedAt;
    }

    public LocalDateTime getSupplierUpdatedAt() {
        return supplierUpdatedAt;
    }

    public void setSupplierUpdatedAt(LocalDateTime supplierUpdatedAt) {
        this.supplierUpdatedAt = supplierUpdatedAt;
    }

    public LocalDateTime getSupplierDeletedAt() {
        return supplierDeletedAt;
    }

    public void setSupplierDeletedAt(LocalDateTime supplierDeletedAt) {
        this.supplierDeletedAt = supplierDeletedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
