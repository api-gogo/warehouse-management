package com.ohgiraffers.warehousemanagement.wms.inventory.service;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;

import java.util.List;

public interface InventoryService {

    // 재고 수정, 삭제 메소드에 필요한 재고 ID에 해당하는 재고 정보 불러오기
    InventoryDTO findInventoryById(Long inventoryId);

    // 입력한 상품 ID에 해당되는 재고 중 유통기한이 가장 얼마 안 남은 재고 정보 불러오기
    Inventory findTopByProductIdOrderByInventoryExpiryDateAsc(Integer productId);

    // 재고 로트 번호 생성
    String createRotNum(Product product);

    // 재고 로트 번호 생성 시 사용되는 일련번호 생성 메서드
    int getNextSequenceForProductToday(Integer productId);



    }
