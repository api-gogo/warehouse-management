package com.ohgiraffers.warehousemanagement.wms.shipments.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shipments")
public class Shipments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "shipment_id")
    private Long shipmentId;
}
