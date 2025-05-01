package com.ohgiraffers.warehousemanagement.wms.returning.model.entity;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturningStorageCause;
import jakarta.persistence.*;

@Entity
@Table(name = "return_storage_items")
public class ReturnStorageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_storage_item_id")
    private Integer returnStorageItemId; //PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_storage_id",nullable = false)
    private ReturnStorage returnStorage; //외래키, 반품번호

//    @Enumerated(EnumType.STRING)
//    @Column(name = "return_storage_content",nullable = false)
//    private ReturningStorageCause returnStorageContent; //반품사유

    @Column(name = "return_storage_quantity")
    private Integer returnStorageQuantity; //수량



    public ReturnStorageItem() {
    }

    public ReturnStorageItem(Integer returnStorageItemId, ReturnStorage returnStorage, ReturningStorageCause returnStorageContent, Integer returnStorageQuantity) {
        this.returnStorageItemId = returnStorageItemId;
        this.returnStorage = returnStorage;
//        this.returnStorageContent = returnStorageContent;
        this.returnStorageQuantity = returnStorageQuantity;
    }

    public Integer getReturnStorageItemId() {
        return returnStorageItemId;
    }

    public void setReturnStorageItemId(Integer returnStorageItemId) {
        this.returnStorageItemId = returnStorageItemId;
    }

    public ReturnStorage getReturnStorage() {
        return returnStorage;
    }

    public void setReturnStorage(ReturnStorage returnStorage) {
        this.returnStorage = returnStorage;
    }

//    public ReturningStorageCause getReturnStorageContent() {
//        return returnStorageContent;
//    }
//
//    public void setReturnStorageContent(ReturningStorageCause returnStorageContent) {
//        this.returnStorageContent = returnStorageContent;
//    }

    public Integer getReturnStorageQuantity() {
        return returnStorageQuantity;
    }

    public void setReturnStorageQuantity(Integer returnStorageQuantity) {
        this.returnStorageQuantity = returnStorageQuantity;
    }

    @Override
    public String toString() {
        return "ReturnStorageItem{" +
                "returnStorageItemId=" + returnStorageItemId +
                ", returnStorageId=" + returnStorage +
//                ", returnStorageContent=" + returnStorageContent +
                ", returnStorageQuantity=" + returnStorageQuantity +
                '}';
    }
}