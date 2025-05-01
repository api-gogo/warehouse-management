package com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO;

public class InventoryViewDTO {

    private Integer productId;
    private String productName;
    private Long Count;
    private Long TotalAvailableStock;
    private Long TotalAllocatedStock;
    private Long TotalDisposedStock;

    public InventoryViewDTO() {
    }

    public InventoryViewDTO(Integer productId, String productName, Long count, Long totalAvailableStock, Long totalAllocatedStock, Long totalDisposedStock) {
        this.productId = productId;
        this.productName = productName;
        Count = count;
        TotalAvailableStock = totalAvailableStock;
        TotalAllocatedStock = totalAllocatedStock;
        TotalDisposedStock = totalDisposedStock;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getCount() {
        return Count;
    }

    public void setCount(Long count) {
        Count = count;
    }

    public Long getTotalAvailableStock() {
        return TotalAvailableStock;
    }

    public void setTotalAvailableStock(Long totalAvailableStock) {
        TotalAvailableStock = totalAvailableStock;
    }

    public Long getTotalAllocatedStock() {
        return TotalAllocatedStock;
    }

    public void setTotalAllocatedStock(Long totalAllocatedStock) {
        TotalAllocatedStock = totalAllocatedStock;
    }

    public Long getTotalDisposedStock() {
        return TotalDisposedStock;
    }

    public void setTotalDisposedStock(Long totalDisposedStock) {
        TotalDisposedStock = totalDisposedStock;
    }

    @Override
    public String toString() {
        return "InventoryViewDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", Count=" + Count +
                ", TotalAvailableStock=" + TotalAvailableStock +
                ", TotalAllocatedStock=" + TotalAllocatedStock +
                ", TotalDisposedStock=" + TotalDisposedStock +
                '}';
    }
}