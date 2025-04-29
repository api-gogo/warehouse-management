package com.ohgiraffers.warehousemanagement.wms.sales.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sales_items")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_items_id")
    private Integer salesItemsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id", nullable = false)
    private Sales salesId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "sales_items_quantity", nullable = false)
    private Integer salesItemsQuantity;

    public SalesItem() {
    }

    private SalesItem(Builder builder) {
        this.salesId = builder.salesId;
        this.productId = builder.productId;
        this.salesItemsQuantity = builder.salesItemsQuantity;
    }

    public static class Builder {
        private Sales salesId;
        private Integer productId;
        private Integer salesItemsQuantity;

        public Builder() {}

        public Builder salesId(Sales salesId) {
            this.salesId = salesId;
            return this;
        }

        public Builder productId(Integer productId) {
            this.productId = productId;
            return this;
        }

        public Builder salesItemsQuantity(Integer salesItemsQuantity) {
            this.salesItemsQuantity = salesItemsQuantity;
            return this;
        }

        public SalesItem build() {
            SalesItem salesItem = new SalesItem();
            salesItem.salesId = salesId;
            salesItem.productId = productId;
            salesItem.salesItemsQuantity = salesItemsQuantity;

            return salesItem;
        }
    }

    public Integer getSalesItemsId() {
        return salesItemsId;
    }

    public Sales getSalesId() {
        return salesId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getSalesItemsQuantity() {
        return salesItemsQuantity;
    }

    @Override
    public String toString() {
        return "SalesItems{" +
                "salesItemsId=" + salesItemsId +
                ", salesId=" + salesId +
                ", productId=" + productId +
                ", salesItemsQuantity=" + salesItemsQuantity +
                '}';
    }
}
