package com.ohgiraffers.warehousemanagement.wms.returning.model.entity;

import com.ohgiraffers.warehousemanagement.wms.returning.model.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.model.ReturningShipmentCause;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "return_shipment_items")
public class ReturnShipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_shipment_item_id") //출고 반품id, PK
    private Integer returnShipmentItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_shipment_id",nullable = false) //반품번호, FK
    private ReturnShipment returnShipmentId;

    @Column(name = "lot_number")
    private Integer lotNumber;

    @Column(name = "return_shipment_quantity")
    private Integer returnShipmentQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_shipment_content",nullable = false)
    private ReturningShipmentCause returnShipmentContent;

    public ReturnShipmentItem() {
    }

    public ReturnShipmentItem(ReturnShipment returnShipmentId, Integer lotNumber, Integer returnShipmentQuantity, ReturningShipmentCause returnShipmentContent) {
        this.returnShipmentId = returnShipmentId;
        this.lotNumber = lotNumber;
        this.returnShipmentQuantity = returnShipmentQuantity;
        this.returnShipmentContent = returnShipmentContent;
    }

    public Integer getReturnShipmentItemId() {
        return returnShipmentItemId;
    }

    public void setReturnShipmentItemId(Integer returnShipmentItemId) {
        this.returnShipmentItemId = returnShipmentItemId;
    }

    public ReturnShipment getReturnShipmentId() {
        return returnShipmentId;
    }

    public void setReturnShipmentId(ReturnShipment returnShipmentId) {
        this.returnShipmentId = returnShipmentId;
    }

    public Integer getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(Integer lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Integer getReturnShipmentQuantity() {
        return returnShipmentQuantity;
    }

    public void setReturnShipmentQuantity(Integer returnShipmentQuantity) {
        this.returnShipmentQuantity = returnShipmentQuantity;
    }

    public ReturningShipmentCause getReturnShipmentContent() {
        return returnShipmentContent;
    }

    public void setReturnShipmentContent(ReturningShipmentCause returnShipmentContent) {
        this.returnShipmentContent = returnShipmentContent;
    }

    @Override
    public String toString() {
        return "ReturnShipmentItem{" +
                "returnShipmentItemId=" + returnShipmentItemId +
                ", returnShipmentId=" + returnShipmentId +
                ", lotNumber=" + lotNumber +
                ", returnShipmentQuantity=" + returnShipmentQuantity +
                ", returnShipmentContent=" + returnShipmentContent +
                '}';
    }
}
