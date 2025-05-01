package com.ohgiraffers.warehousemanagement.wms.purchases.service;


import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseItemDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseStatus;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.repository.PurchaseItemRepository;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.repository.PurchaseRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchasesItemService {
    private final PurchaseItemRepository purchaseItemRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchasesItemService(PurchaseItemRepository purchaseItemRepository, PurchaseRepository purchaseRepository) {
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseRepository = purchaseRepository;
    }
    
    /**
     * 발주 ID와 관련된 모든 발주 항목을 삭제합니다.
     * @param purchaseId 삭제할 발주 항목의 발주 ID
     */
    @Transactional
    public void deleteAllByPurchaseId(Integer purchaseId) {
        // 발주 조회 및 상태 확인
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다. ID: " + purchaseId));

        if (purchase.getPurchaseStatus() != PurchaseStatus.대기) {
            throw new IllegalStateException("발주대기 상태일 때만 항목을 삭제할 수 있습니다.");
        }
        
        // 발주 ID로 모든 발주 항목 삭제
        purchaseItemRepository.deleteByPurchasePurchaseId(purchaseId);
    }


    // 단일 조회
    public PurchaseItemDTO getPurchaseItemById(Integer purchaseItemId) {
        PurchaseItem purchaseItem = purchaseItemRepository.findById(purchaseItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 발주 항목이 없습니다. ID: " + purchaseItemId));

        return new PurchaseItemDTO(
                purchaseItem.getPurchaseItemId(),
                purchaseItem.getPurchase().getPurchaseId(),
                purchaseItem.getProductId(),
                purchaseItem.getProductQuantity(),
                null, // Product 객체는 필요에 따라 별도 조회
                null, // productName은 필요에 따라 별도 조회
                null, // pricePerBox는 필요에 따라 별도 조회
                null  // quantityPerBox는 필요에 따라 별도 조회
        );
    }


    //생성
    @Transactional
    public PurchaseItemDTO createPurchaseItem(PurchaseItemDTO purchaseItemDTO) {
        // 발주 조회 및 상태 확인
        Purchase purchase = purchaseRepository.findById(purchaseItemDTO.getPurchaseId())
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다. ID: " + purchaseItemDTO.getPurchaseId()));

        if (!PurchaseStatus.대기.equals(purchase.getPurchaseStatus())) {
            throw new IllegalStateException("발주대기 상태일 때만 항목을 추가할 수 있습니다.");
        }

        // PurchaseItem 엔티티 생성
        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setPurchase(purchase);
        purchaseItem.setProductId(purchaseItemDTO.getProductId());
        purchaseItem.setProductQuantity(purchaseItemDTO.getProductQuantity());

        // 데이터베이스에 저장
        PurchaseItem savedPurchaseItem = purchaseItemRepository.save(purchaseItem);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return new PurchaseItemDTO(
                savedPurchaseItem.getPurchaseItemId(),
                savedPurchaseItem.getPurchase().getPurchaseId(),
                savedPurchaseItem.getProductId(),
                savedPurchaseItem.getProductQuantity(),
                null,
                null,
                null,
                null
        );
    /*
        public BoardDTO findBoardById(Integer boardId) {
        //1. 입력한  board를 id를 기준으로 데이버 베이스에서 꺼내온다.
        //2. 해당 board를 반환한다.
        Board board = boardRepository.findByBoardId(boardId)

                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        //Board는 DB와 매핑되는 엔티티 객체이고, BoardDTO는 클라이언트에게
        // 전달되는 데이터 전송 객체이기 떄문에 전송을 위해 다시 dto에 담아서 리턴함
        BoardDTO boardDTO = new BoardDTO(
                board.getBoardId(),
                board.getBoardTitle(),
                board.getBoardContent()
        );

        return boardDTO;
    }
     */
    }
}
