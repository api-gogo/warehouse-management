package com.ohgiraffers.warehousemanagement.wms.sales.model.dto;

import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesDTO {
    private Integer salesId;
    private Integer storeId;
    private String storeName;
    private String storeAddress;
    private Integer userId;
    private String userName;
    private String userPhone;
    private LocalDate salesDate;
    private LocalDate shippingDueDate;
    private SalesStatus salesStatus;
    private LocalDateTime salesCreatedAt;
    private LocalDateTime salesUpdatedAt;
    private List<Integer> productIds;
    private List<String> productNames; // 상품명
    private List<Integer> quantity;
    private List<Integer> pricePerBox; // 박스당단가
    private List<Integer> totalPrice; // 총액

    public SalesDTO() {
    }

    public SalesDTO(Integer salesId, Integer storeId, String storeName, String storeAddress, Integer userId, String userName, String userPhone, LocalDate salesDate, LocalDate shippingDueDate, SalesStatus salesStatus, LocalDateTime salesCreatedAt, LocalDateTime salesUpdatedAt) {
        this.salesId = salesId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.salesDate = salesDate;
        this.shippingDueDate = shippingDueDate;
        this.salesStatus = salesStatus;
        this.salesCreatedAt = salesCreatedAt;
        this.salesUpdatedAt = salesUpdatedAt;
    }

    public Integer getSalesId() {
        return salesId;
    }

    public void setSalesId(Integer salesId) {
        this.salesId = salesId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public LocalDate getShippingDueDate() {
        return shippingDueDate;
    }

    public void setShippingDueDate(LocalDate shippingDueDate) {
        this.shippingDueDate = shippingDueDate;
    }

    public SalesStatus getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(SalesStatus salesStatus) {
        this.salesStatus = salesStatus;
    }

    public LocalDateTime getSalesCreatedAt() {
        return salesCreatedAt;
    }

    public void setSalesCreatedAt(LocalDateTime salesCreatedAt) {
        this.salesCreatedAt = salesCreatedAt;
    }

    public LocalDateTime getSalesUpdatedAt() {
        return salesUpdatedAt;
    }

    public void setSalesUpdatedAt(LocalDateTime salesUpdatedAt) {
        this.salesUpdatedAt = salesUpdatedAt;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public List<String> getProductNames() {
        return productNames;
    }

    public void setProductNames(List<String> productNames) {
        this.productNames = productNames;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }

    public List<Integer> getPricePerBox() {
        return pricePerBox;
    }

    public void setPricePerBox(List<Integer> pricePerBox) {
        this.pricePerBox = pricePerBox;
    }

    public List<Integer> getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(List<Integer> totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "SalesDTO{" +
                "salesId=" + salesId +
                ", storeId=" + storeId +
                ", storeAddress='" + storeAddress + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", salesDate=" + salesDate +
                ", shippingDueDate=" + shippingDueDate +
                ", salesStatus=" + salesStatus +
                ", salesCreatedAt=" + salesCreatedAt +
                ", salesUpdatedAt=" + salesUpdatedAt +
                ", productIds=" + productIds +
                ", productNames=" + productNames +
                ", quantity=" + quantity +
                ", pricePerBox=" + pricePerBox +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
