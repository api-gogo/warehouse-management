package com.ohgiraffers.warehousemanagement.wms.supplier.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_name", length = 50, nullable = false, unique = true)
    private String supplierName;

    @Column(name = "supplier_address", length = 100, nullable = false)
    private String supplierAddress;

    @Column(name = "supplier_manager_name", length = 50, nullable = false)
    private String supplierManagerName;

    @Column(name = "supplier_manager_phone", length = 50, nullable = false, unique = true)
    private String supplierManagerPhone;

    @Column(name = "supplier_manager_email", length = 100, nullable = false, unique = true)
    private String supplierManagerEmail;

    @Column(name = "supplier_created_at", nullable = false)
    private LocalDateTime supplierCreatedAt;

    @Column(name = "supplier_updated_at")
    private LocalDateTime supplierUpdateAt;

    @Column(name = "supplier_deleted_at")
    private LocalDateTime supplierDeletedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    protected Supplier() {}

    public Supplier(String supplierManagerEmail, String supplierManagerPhone, String supplierManagerName, String supplierAddress, String supplierName) {
        this.supplierManagerEmail = supplierManagerEmail;
        this.supplierManagerPhone = supplierManagerPhone;
        this.supplierManagerName = supplierManagerName;
        this.supplierAddress = supplierAddress;
        this.supplierName = supplierName;
        this.supplierCreatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public String getSupplierManagerName() {
        return supplierManagerName;
    }

    public String getSupplierManagerPhone() {
        return supplierManagerPhone;
    }

    public String getSupplierManagerEmail() {
        return supplierManagerEmail;
    }

    public LocalDateTime getSupplierCreatedAt() {
        return supplierCreatedAt;
    }

    public LocalDateTime getSupplierUpdateAt() {
        return supplierUpdateAt;
    }

    public LocalDateTime getSupplierDeletedAt() {
        return supplierDeletedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public void setSupplierManagerName(String supplierManagerName) {
        this.supplierManagerName = supplierManagerName;
    }

    public void setSupplierManagerPhone(String supplierManagerPhone) {
        this.supplierManagerPhone = supplierManagerPhone;
    }

    public void setSupplierManagerEmail(String supplierManagerEmail) {
        this.supplierManagerEmail = supplierManagerEmail;
    }

    public void setSupplierUpdateAt(LocalDateTime supplierUpdateAt) {
        this.supplierUpdateAt = supplierUpdateAt;
    }

    public void setSupplierDeletedAt(LocalDateTime supplierDeletedAt) {
        this.supplierDeletedAt = supplierDeletedAt;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
