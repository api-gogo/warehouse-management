package com.ohgiraffers.warehousemanagement.wms.product.model.DTO;

import java.util.List;

public class ProductPageResponseDTO {
    private List<ProductResponseDTO> products;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int startItem;
    private int endItem;

    public ProductPageResponseDTO() {}

    public ProductPageResponseDTO(List<ProductResponseDTO> products, int currentPage, int totalPages, long totalItems, int startItem, int endItem) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.startItem = startItem;
        this.endItem = endItem;
    }

    // Getter와 Setter
    public List<ProductResponseDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponseDTO> products) {
        this.products = products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getStartItem() {
        return startItem;
    }

    public void setStartItem(int startItem) {
        this.startItem = startItem;
    }

    public int getEndItem() {
        return endItem;
    }

    public void setEndItem(int endItem) {
        this.endItem = endItem;
    }
}