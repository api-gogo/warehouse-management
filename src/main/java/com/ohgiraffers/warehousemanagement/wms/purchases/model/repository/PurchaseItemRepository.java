package com.ohgiraffers.warehousemanagement.wms.purchases.model.repository;

import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    
    // 구매 ID로 해당 구매에 포함된 모든 항목 찾기
    List<PurchaseItem> findByPurchasePurchaseId(Integer purchaseId);
    
    // 구매 ID로 해당 구매에 포함된 모든 항목 삭제
    void deleteByPurchasePurchaseId(Integer purchaseId);
}