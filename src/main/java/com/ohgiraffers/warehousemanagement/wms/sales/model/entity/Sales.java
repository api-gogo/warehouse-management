package com.ohgiraffers.warehousemanagement.wms.sales.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_id")
    private Integer salesId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId; // 점포

    @Column(name = "user_id", nullable = false)
    private Integer userId; // 수주 담당자(작업자)

    @Column(name = "sales_date", nullable = false)
    private LocalDate salesDate; // 수주일

    @Column(name = "shipping_due_date", nullable = false)
    private LocalDate shippingDueDate; // 출고예정일

    @Column(name = "sales_status", nullable = false)
    private SalesStatus salesStatus;

    @Column(name = "sales_created_at", nullable = false)
    private LocalDateTime salesCreatedAt;

    @Column(name = "sales_updated_at")
    private LocalDateTime salesUpdatedAt;

    public Sales() {
    }

    private Sales(Builder builder) {
        this.storeId = builder.storeId;
        this.userId = builder.userId;
        this.salesDate = builder.salesDate;
        this.shippingDueDate = builder.shippingDueDate;
        this.salesStatus = builder.salesStatus;
        this.salesCreatedAt = builder.salesCreatedAt;
        this.salesUpdatedAt = builder.salesUpdatedAt;
    }

    public static class Builder {
        private Integer storeId;
        private Integer userId;
        private LocalDate salesDate;
        private LocalDate shippingDueDate;
        private SalesStatus salesStatus;
        private LocalDateTime salesCreatedAt;
        private LocalDateTime salesUpdatedAt;

        public Builder() {}

        public Builder storeId(Integer storeId) {
            this.storeId = storeId;
            return this;
        }
        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }
        public Builder salesDate(LocalDate salesDate) {
            this.salesDate = salesDate;
            return this;
        }
        public Builder shippingDueDate(LocalDate shippingDueDate) {
            this.shippingDueDate = shippingDueDate;
            return this;
        }
        public Builder salesStatus(SalesStatus salesStatus) {
            this.salesStatus = salesStatus;
            return this;
        }
        public Builder salesCreatedAt(LocalDateTime salesCreatedAt) {
            this.salesCreatedAt = salesCreatedAt;
            return this;
        }
        public Builder salesUpdatedAt(LocalDateTime salesUpdatedAt) {
            this.salesUpdatedAt = salesUpdatedAt;
            return this;
        }
        public Sales build() {
            return new Sales(this);
        }
    }

    public Integer getSalesId() {
        return salesId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public LocalDate getShippingDueDate() {
        return shippingDueDate;
    }

    public SalesStatus getSalesStatus() {
        return salesStatus;
    }

    public LocalDateTime getSalesCreatedAt() {
        return salesCreatedAt;
    }

    public LocalDateTime getSalesUpdatedAt() {
        return salesUpdatedAt;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "salesId=" + salesId +
                ", storeId=" + storeId +
                ", userId=" + userId +
                ", salesDate=" + salesDate +
                ", shippingDueDate=" + shippingDueDate +
                ", salesStatus=" + salesStatus +
                ", salesCreatedAt=" + salesCreatedAt +
                ", salesUpdatedAt=" + salesUpdatedAt +
                '}';
    }
}
