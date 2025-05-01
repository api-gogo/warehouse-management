package com.ohgiraffers.warehousemanagement.wms.sales.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long userId; // 수주 담당자(작업자)

    @Column(name = "sales_date", nullable = false)
    private LocalDate salesDate; // 수주일

    @Column(name = "shipping_due_date", nullable = false)
    private LocalDate shippingDueDate; // 출고예정일

    @Enumerated(EnumType.STRING)
    @Column(name = "sales_status", nullable = false)
    private SalesStatus salesStatus;

    @Column(name = "sales_created_at", nullable = false)
    private LocalDateTime salesCreatedAt;

    @Column(name = "sales_updated_at")
    private LocalDateTime salesUpdatedAt;

    // Sales를 지우면 SalesItem도 지워짐. 업데이트되어도 똑같음
    @OneToMany(mappedBy = "salesId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesItem> salesItems = new ArrayList<>();

    public Sales() {
    }

    private Sales(Builder builder) {
        this.storeId = builder.storeId;
        this.userId = builder.userId;
        this.salesDate = builder.salesDate;
        this.shippingDueDate = builder.shippingDueDate;
        this.salesStatus = builder.salesStatus;
        this.salesCreatedAt = builder.salesCreatedAt;
    }

    public static class Builder {
        private Integer storeId;
        private Long userId;
        private LocalDate salesDate;
        private LocalDate shippingDueDate;
        private SalesStatus salesStatus;
        private LocalDateTime salesCreatedAt;

        public Builder() {}

        public Builder storeId(Integer storeId) {
            this.storeId = storeId;
            return this;
        }
        public Builder userId(Long userId) {
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

        public Sales build() {
            Sales sales = new Sales();
            sales.storeId = storeId;
            sales.userId = userId;
            sales.salesDate = salesDate;
            sales.shippingDueDate = shippingDueDate;
            sales.salesStatus = this.salesStatus != null ? this.salesStatus : SalesStatus.PENDING;
            sales.salesCreatedAt = this.salesCreatedAt != null? this.salesCreatedAt : LocalDateTime.now();

            return sales;
        }
    }

    public Integer getSalesId() {
        return salesId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public Long getUserId() {
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

    public List<SalesItem> getSalesItems() {
        return salesItems;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public void setShippingDueDate(LocalDate shippingDueDate) {
        this.shippingDueDate = shippingDueDate;
    }

    public void setSalesStatus(SalesStatus salesStatus) {
        this.salesStatus = salesStatus;
    }

    public void setSalesUpdatedAt(LocalDateTime salesUpdatedAt) {
        this.salesUpdatedAt = salesUpdatedAt;
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
