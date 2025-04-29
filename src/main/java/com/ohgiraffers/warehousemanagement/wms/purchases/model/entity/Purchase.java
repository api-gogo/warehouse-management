package com.ohgiraffers.warehousemanagement.wms.purchases.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {

//    @ManyToOne(fetch = FetchType.LAZY) // 거래처와 연결 다대일 관계 여러개의 발주가 거래처 하나만 보유 가능
//    @JoinColumn(name = "supplier_id")
//    private Supplier supplier;
//    //Purchase 엔티티에 연결된 실제 거래처 정보를 담는 필드
//

   // 링크 테이블로 연결되어 있음 여러개의 발주가 여러개의 상품을 가질수도 여러개의 상품이 여러개의 발주를 가질수도 있음
     @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<PurchaseItem> items = new ArrayList<>();


//    @OneToOne(mappedBy = "purchase") // UserProfile에서 참조하는 쪽에서 mappedBy 사용
//    private Storages storages; //입고에서 참고 함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Integer purchaseId;

    // 발주 담당자
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_due_date")
    private LocalDate purchaseDueDate;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @Column(name = "purchase_created_at")
    private LocalDate purchaseCreatedAt;

    @Column(name = "purchase_updated_at")
    private LocalDate purchaseUpdatedAt;


    // 비고란
    @Column(name = "notes")
    private String notes;


//    @ManyToOne(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Integer userId;

    public Purchase() {}



    public Purchase(
                    Integer purchaseId,
                    Integer userId, LocalDate purchaseDate, LocalDate purchaseDueDate,
                    PurchaseStatus purchaseStatus, LocalDate purchaseCreatedAt,
                    LocalDate purchase_updated_at, String notes) {
        //Supplier supplier, Storages storages,


//        this.supplier = supplier;
//        this.storages = storages;
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.purchaseDueDate = purchaseDueDate;
        this.purchaseStatus = purchaseStatus;
        this.purchaseCreatedAt = purchaseCreatedAt;
        this.purchaseUpdatedAt = purchase_updated_at;
        this.notes = notes;
    }



//    public Supplier getSupplier() {
//        return supplier;
//    }
//
//    public void setSupplier(Supplier supplier) {
//        this.supplier = supplier;
//    }
//
    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }
//
//    public Storages getStorages() {
//        return storages;
//    }
//
//    public void setStorages(Storages storages) {
//        this.storages = storages;
//    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getPurchaseDueDate() {
        return purchaseDueDate;
    }

    public void setPurchaseDueDate(LocalDate purchaseDueDate) {
        this.purchaseDueDate = purchaseDueDate;
    }

    public PurchaseStatus getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(PurchaseStatus purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public LocalDate getPurchaseCreatedAt() {
        return purchaseCreatedAt;
    }

    public void setPurchaseCreatedAt(LocalDate purchaseCreatedAt) {
        this.purchaseCreatedAt = purchaseCreatedAt;
    }

    public LocalDate getPurchaseUpdatedAt() {
        return purchaseUpdatedAt;
    }

    public void setPurchaseUpdatedAt(LocalDate purchase_updated_at) {
        this.purchaseUpdatedAt = purchase_updated_at;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseId=" + purchaseId +
                ", userId=" + userId +
                ", purchaseDate=" + purchaseDate +
//              ", supplier=" + supplier +
                ", items=" + items +
//                ", storages=" + storages +
                ", purchaseDueDate=" + purchaseDueDate +
                ", purchaseStatus='" + purchaseStatus + '\'' +
                ", purchaseCreatedAt=" + purchaseCreatedAt +
                ", purchase_updated_at=" + purchaseUpdatedAt +
                ", notes='" + notes + '\'' +
                '}';
    }
}
