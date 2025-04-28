package com.ohgiraffers.warehousemanagement.purchases.model.repository;

import com.ohgiraffers.warehousemanagement.purchases.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.purchases.model.entity.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByPurchaseStatusOrderByPurchaseCreatedAtAsc(String status);
    List<Purchase> findByPurchaseStatus(PurchaseStatus purchaseStatus);
    List<Purchase> findByUserId(Integer userId);
    List<Purchase> findByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);


}
