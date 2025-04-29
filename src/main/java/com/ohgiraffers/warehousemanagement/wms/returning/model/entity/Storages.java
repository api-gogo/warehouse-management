package com.ohgiraffers.warehousemanagement.wms.returning.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "storages")
public class Storages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id",nullable = false)

    private Integer storageId;

    public Storages(){
    }

    public Storages(Integer storageId) {
        this.storageId = storageId;
    }

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    @Override
    public String toString() {
        return "Storages{" +
                "storageId=" + storageId +
                '}';
    }
}
