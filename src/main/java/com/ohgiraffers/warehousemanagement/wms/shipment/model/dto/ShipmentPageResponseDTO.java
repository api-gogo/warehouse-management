package com.ohgiraffers.warehousemanagement.wms.shipment.model.dto;

import java.util.List;

public class ShipmentPageResponseDTO {

    private List<ShipmentResponseDTO> shipments;
    private int currentPage;
    private int totalPages;
    private int size;
    private int totalItems;
    private int startItem;
    private int endItem;

    public ShipmentPageResponseDTO() {}

    public ShipmentPageResponseDTO(List<ShipmentResponseDTO> shipments, int currentPage, int totalPages, int size,
                                    int totalItems, int startItem, int endItem) {
        this.shipments = shipments;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.size = size;
        this.totalItems = totalItems;
        this.startItem = startItem;
        this.endItem = endItem;
    }

    public List<ShipmentResponseDTO> getShipments() {
        return shipments;
    }

    public void setShipments(List<ShipmentResponseDTO> shipments) {
        this.shipments = shipments;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
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