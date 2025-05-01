package com.ohgiraffers.warehousemanagement.wms.purchases.service;


import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.product.model.repository.ProductRepository;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseItemDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseStatus;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.repository.PurchaseItemRepository;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.repository.PurchaseRepository;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.LoginUserDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;


    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
    }


    public PurchaseItemDTO getProductInfo(Integer productId) {
        // Native Query를 사용한 기존 메서드
        return purchaseRepository.findProductInfo(productId);
    }







    //모든 발주 조회
    public List<PurchaseDTO> getAllPurchases() {
        List<PurchaseDTO> purchasesDTOs = new ArrayList<>();

        // 오름차순 정렬 기능
        Sort sort = Sort.by(Sort.Order.asc("purchaseDate"));

        // sort 파라미터 적용
        for (Purchase purchase : purchaseRepository.findAll(sort)) {
            PurchaseDTO purchaseDTO = new PurchaseDTO(
                    purchase.getPurchaseId(),
                    purchase.getUserId(),
                    purchase.getPurchaseCreatedAt(),
                    purchase.getPurchaseDate(),
                    purchase.getPurchaseDueDate(),
                    purchase.getPurchaseStatus().getLabel(),
                    purchase.getPurchaseUpdatedAt(),
                    purchase.getNotes()
            );
            purchasesDTOs.add(purchaseDTO);
        }
        return purchasesDTOs;
    }

    // 입력한 상태에 맞는 발주 목록만 출력
    public List<PurchaseDTO> searchByStatus(PurchaseStatus status) {


        List<Purchase> purchases = purchaseRepository.findByPurchaseStatus(status);
        List<PurchaseDTO> purchasesDTOs = new ArrayList<>();
        for (Purchase purchase : purchases) {
            PurchaseDTO purchaseDTO = new PurchaseDTO(
                    purchase.getPurchaseId(),
                    purchase.getUserId(),
                    purchase.getPurchaseCreatedAt(),
                    purchase.getPurchaseDate(),
                    purchase.getPurchaseDueDate(),
                    purchase.getPurchaseStatus().getLabel(),
                    purchase.getPurchaseUpdatedAt(),
                    purchase.getNotes()
            );
            purchasesDTOs.add(purchaseDTO);
        }
        return purchasesDTOs;
    }

    //담당자를 이용한 조회
    public List<PurchaseDTO> searchByUserId(Integer userId) {
        List<Purchase> purchases = purchaseRepository.findByUserId(userId);
        List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        for (Purchase purchase : purchases) {
            PurchaseDTO dto = new PurchaseDTO(
                    purchase.getPurchaseId(),
                    purchase.getUserId(),
                    purchase.getPurchaseCreatedAt(),
                    purchase.getPurchaseDate(),
                    purchase.getPurchaseDueDate(),
                    purchase.getPurchaseStatus().getLabel(),
                    purchase.getPurchaseUpdatedAt(),
                    purchase.getNotes()
            );
            purchaseDTOs.add(dto);
        }
        return purchaseDTOs;
    }

    //기간을 이용하여서 조회
    public List<PurchaseDTO> searchByPurchaseDateRange(LocalDate startDate, LocalDate endDate) {
        List<Purchase> purchases = purchaseRepository.findByPurchaseDateBetween(startDate, endDate);
        List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
        for (Purchase purchase : purchases) {
            PurchaseDTO dto = new PurchaseDTO(
                    purchase.getPurchaseId(),
                    purchase.getUserId(),
                    purchase.getPurchaseCreatedAt(),
                    purchase.getPurchaseDate(),
                    purchase.getPurchaseDueDate(),
                    purchase.getPurchaseStatus().getLabel(),
                    purchase.getPurchaseUpdatedAt(),
                    purchase.getNotes()
            );
            purchaseDTOs.add(dto);
        }
        return purchaseDTOs;
    }



    // 단일 발주 조회
    public PurchaseDTO getPurchaseById(Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 발주가 없습니다."));
        PurchaseDTO purchaseDTO = new PurchaseDTO(
                purchase.getPurchaseId(),
                purchase.getUserId(),
                purchase.getPurchaseCreatedAt(),
                purchase.getPurchaseDate(),
                purchase.getPurchaseDueDate(),
                purchase.getPurchaseStatus().getLabel(),
                purchase.getPurchaseUpdatedAt(),
                purchase.getNotes()
        );
        // PurchaseItem 목록 추가
        List<PurchaseItemDTO> items = getPurchaseItemsByPurchaseId(id);
        purchaseDTO.setItems(items);
        return purchaseDTO;
    }
    public List<PurchaseItemDTO> getPurchaseItemsByPurchaseId(Integer purchaseId) {
        List<PurchaseItem> items = purchaseItemRepository.findByPurchasePurchaseId(purchaseId);
        List<PurchaseItemDTO> itemDTOs = new ArrayList<>();
        for (PurchaseItem item : items) {
            PurchaseItemDTO itemDTO = getProductInfo(item.getProductId());
            if (itemDTO != null) {
                itemDTO.setPurchaseItemId(item.getPurchaseItemId());
                itemDTO.setPurchaseId(item.getPurchase().getPurchaseId());
                itemDTO.setProductQuantity(item.getProductQuantity());
                itemDTOs.add(itemDTO);
            } else {
                // 기본 정보로만 DTO 생성
                itemDTOs.add(new PurchaseItemDTO(
                        item.getPurchaseItemId(),
                        item.getPurchase().getPurchaseId(),
                        item.getProductId(),
                        item.getProductQuantity(),
                        null, null, null, null
                ));
            }
        }
        return itemDTOs;
    }


    // 발주 생성
    @Transactional
    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) {
        // userId가 설정되었는지 확인
        if (purchaseDTO.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID가 설정되지 않았습니다.");
        }
        
        // 컨트롤러에서 설정한 userId를 직접 사용
        Purchase purchase = new Purchase(
                null, // ID는 자동 생성
                purchaseDTO.getUserId(), // 컨트롤러에서 설정한 사용자 ID 사용
                purchaseDTO.getPurchaseDate() != null ? purchaseDTO.getPurchaseDate() : LocalDate.now(),
                purchaseDTO.getPurchaseDueDate() != null ? purchaseDTO.getPurchaseDueDate() : LocalDate.now().plusDays(7),
                PurchaseStatus.대기,
                LocalDate.now(),
                null,
                purchaseDTO.getPurchaseNotes()
        );

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return new PurchaseDTO(
                savedPurchase.getPurchaseId(),
                savedPurchase.getUserId(),
                savedPurchase.getPurchaseCreatedAt(),
                savedPurchase.getPurchaseDate(),
                savedPurchase.getPurchaseDueDate(),
                savedPurchase.getPurchaseStatus().getLabel(),
                savedPurchase.getPurchaseUpdatedAt(),
                savedPurchase.getNotes()
        );
    }



    // 발주 수정
    @Transactional
    public PurchaseDTO updatePurchase(PurchaseDTO purchaseDTO, Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다."));

        // 해당 발주의 status가 발주대기라면 수정을하고 발주대기가 아니라면 오류를 표시함
        if(PurchaseStatus.대기.equals(purchase.getPurchaseStatus())) {
            // 기존에는 status를 항상 '대기'로 설정했으나, 이제는 선택된 status로 설정
            // purchaseDTO에서 문자열로 받은 status를 enum으로 변환
            PurchaseStatus status;
            try {
                // 수 purchaseDTO.getPurchaseStatus()가 enum의 name 또는 label이 될 수 있음
                status = getStatusFromString(purchaseDTO.getPurchaseStatus());
            } catch (IllegalArgumentException e) {
                // 기본값으로 '대기' 상태 설정
                status = PurchaseStatus.대기;
            }
            
            // 사용자가 수정한 정보로 엔티티 업데이트
            purchase.setPurchaseStatus(status);
            purchase.setUserId(purchaseDTO.getUserId()); // userId 업데이트 추가
            purchase.setPurchaseDate(purchaseDTO.getPurchaseDate());
            purchase.setPurchaseDueDate(purchaseDTO.getPurchaseDueDate());
            purchase.setPurchaseCreatedAt(purchaseDTO.getPurchaseCreatedAt());
            purchase.setPurchaseUpdatedAt(LocalDate.now());
            purchase.setNotes(purchaseDTO.getPurchaseNotes()); // notes 업데이트 추가
            
            Purchase updatepurchase = purchaseRepository.save(purchase);

            return new PurchaseDTO(
                    updatepurchase.getPurchaseId(),
                    updatepurchase.getUserId(),
                    updatepurchase.getPurchaseCreatedAt(),
                    updatepurchase.getPurchaseDate(),
                    updatepurchase.getPurchaseDueDate(),
                    updatepurchase.getPurchaseStatus().getLabel(),
                    updatepurchase.getPurchaseUpdatedAt(),
                    updatepurchase.getNotes()
            );
        } else {
            throw new IllegalArgumentException("발주대기가 아니라면 수정을 못합니다.");
            // 발주대기 상태가 아니라면 수정 못한다는 것을 표시
        }
    }
    
    // 문자열 상태를 PurchaseStatus Enum으로 변환하는 헬퍼 메서드
    private PurchaseStatus getStatusFromString(String statusStr) {
        // 직접 Enum 이름으로 매칭 시도
        try {
            return PurchaseStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            // Enum 이름으로 매칭 실패 시, label로 매칭 시도
            for (PurchaseStatus status : PurchaseStatus.values()) {
                if (status.getLabel().equals(statusStr)) {
                    return status;
                }
            }
            // 두 방식 모두 실패하면 예외 발생
            throw new IllegalArgumentException("Invalid status: " + statusStr);
        }
    }
    


    /**
     * 발주 항목 삭제
     * @param purchaseId 발주 ID
     * @param itemId 항목 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean deletePurchaseItem(Integer purchaseId, Integer itemId) {
        // 1. 발주 항목 조회
        PurchaseItem purchaseItem = purchaseItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주 항목이 없습니다."));
                
        // 2. 발주가 발주대기 상태인지 확인
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다."));
                
        if (!PurchaseStatus.대기.equals(purchase.getPurchaseStatus())) {
            throw new IllegalStateException("발주대기 상태일 때만 상품을 삭제할 수 있습니다.");
        }
        
        // 3. 항목이 해당 발주에 속하는지 확인
        if (!purchaseItem.getPurchase().getPurchaseId().equals(purchaseId)) {
            throw new IllegalArgumentException("해당 발주에 속한 항목이 아닙니다.");
        }
        
        // 4. 발주 항목 삭제
        purchaseItemRepository.delete(purchaseItem);
        
        return true;
    }


    @Transactional
    public boolean deletedpurchase(Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다. ID: " + id));

        purchaseRepository.delete(purchase); // 엔티티를 직접 삭제

        return true; // 삭제가 완료되었으면 true 반환
    }


    @Transactional
    public boolean completePurchase(Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

        //해당 발주의 status가 발주대기라면 status를 발주완료로 바꾸고 발주대기가 아니라면 오류를 표시함
        if (PurchaseStatus.대기.equals(purchase.getPurchaseStatus())) {
            purchase.setPurchaseStatus(PurchaseStatus.완료);
            purchaseRepository.save(purchase);
            return true;
        } else {
            // 상태가 발주대기가 아니여서 완료를 못한다고 알려줌
            return false;
        }
    }
   @Transactional
    public boolean rejectPurchase(Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다."));
       //해당 발주의 status가 발주대기라면 status를 발주거절로 바꾸고 발주대기가 아니라면 오류를 표시함
        if (PurchaseStatus.대기.equals(purchase.getPurchaseStatus())) {
            purchase.setPurchaseStatus(PurchaseStatus.거절);
            purchaseRepository.save(purchase);
            return true;
        } else {
            //발주대기가 아니라면 안된다는 오류 표시
            return false;
        }
    }
    @Transactional
    public boolean cancelPurchase(Integer id) {

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다."));
        //해당 발주의 status가 발주대기라면 status를 발주취소로 바꾸고 발주대기가 아니라면 오류를 표시함
        if (PurchaseStatus.대기.equals(purchase.getPurchaseStatus())) {
            purchase.setPurchaseStatus(PurchaseStatus.취소);
            purchaseRepository.save(purchase);
            return true;
        } else {
            // 발주대기가 아니라면 취소를 못한다는 오류 표시
            return false;
        }

    }

    


//    // 페이징 처리된 발주 목록 조회
//    public Page<Purchase> getPurchasesWithPagination(Pageable pageable) {
//        return purchaseRepository.findAll(pageable);
//    }
//
//    // 검색 조건이 있는 페이징 처리
//    public Page<Purchase> searchPurchasesWithPagination(
//            String keyword,
//            String status,
//            LocalDate startDate,
//            LocalDate endDate,
//            Pageable pageable) {
//
//        // 검색 조건에 따른 분기 처리
//        if (status != null && !status.isEmpty()) {
//            if (startDate != null && endDate != null) {
//                // 상태 + 날짜 범위 검색
//                LocalDateTime start = startDate.atStartOfDay();
//                LocalDateTime end = endDate.atTime(23, 59, 59);
//                return purchaseRepository.findByPurchaseDateBetweenAndPurchaseStatusContaining(
//                        start, end, status, pageable);
//            } else {
//                // 상태만 검색
//                return purchaseRepository.findByPurchaseStatusContaining(status, pageable);
//            }
//        } else {
//            // 기본 페이징
//            return purchaseRepository.findAll(pageable);
//        }
//    }



}