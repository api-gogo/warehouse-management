package com.ohgiraffers.warehousemanagement.service;


import com.ohgiraffers.warehousemanagement.model.dto.PurchaseDTO;
import com.ohgiraffers.warehousemanagement.model.entity.Purchase;
import com.ohgiraffers.warehousemanagement.model.entity.PurchaseStatus;
import com.ohgiraffers.warehousemanagement.model.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;



    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }


    //모든 발주 조회
    public List<PurchaseDTO> getAllPurchases() {
        List<PurchaseDTO> purchasesDTOs = new ArrayList<>();


        Sort sort = Sort.by(Sort.Order.asc("purchaseCreatedAt")); // 오름차순 정렬 기능
        for (Purchase purchase : purchaseRepository.findAll(sort)) {
            PurchaseDTO purchaseDTO = new PurchaseDTO(
                    purchase.getPurchaseId(),
                    purchase.getUserId(),
                    purchase.getPurchaseCreatedAt(),
                    purchase.getPurchaseDate(),
                    purchase.getPurchaseDueDate(),
                    purchase.getPurchaseStatus().getLabel(),
                    purchase.getPurchaseUpdatedAt()
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
                    purchase.getPurchaseUpdatedAt()
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
                    purchase.getPurchaseUpdatedAt()
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
                    purchase.getPurchaseUpdatedAt()
            );
            purchaseDTOs.add(dto);
        }
        return purchaseDTOs;
    }



    // 단일 발주 조회
    public PurchaseDTO getPurchaseById(Integer id) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 발주가 없습니다."));
        PurchaseDTO purchaseDTO = new PurchaseDTO(
                purchase.getPurchaseId(),
                purchase.getUserId(),
                purchase.getPurchaseCreatedAt(),
                purchase.getPurchaseDate(),
                purchase.getPurchaseDueDate(),
                purchase.getPurchaseStatus().getLabel(),
                purchase.getPurchaseUpdatedAt()
        );
        return purchaseDTO;

    }


    // 발주 생성
    @Transactional
    public PurchaseDTO createPurchase(PurchaseDTO purchaseDTO) {
        Purchase purchase = new Purchase(
                purchaseDTO.getPurchaseId(),
                purchaseDTO.getUserId(),
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                 PurchaseStatus.대기,
                 LocalDate.now(),
                null
        );

        Purchase savepurchase = purchaseRepository.save(purchase);

        return new PurchaseDTO(
                savepurchase.getPurchaseId(),
                savepurchase.getUserId(),
                savepurchase.getPurchaseCreatedAt(),
                savepurchase.getPurchaseDate(),
                savepurchase.getPurchaseDueDate(),
                savepurchase.getPurchaseStatus().getLabel(),
                savepurchase.getPurchaseUpdatedAt()
        );
    }



    // 발주 수정
    @Transactional
    public PurchaseDTO updatePurchase(PurchaseDTO purchaseDTO,Integer id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 발주가 없습니다."));


        //해당 발주의 status가 발주대기라면 수정을하고 발주대기가 아니라면 오류를 표시함
        if(PurchaseStatus.대기.equals(purchase.getPurchaseStatus())) {
            purchase.setPurchaseStatus(PurchaseStatus.대기);
            purchase.setPurchaseDate(purchaseDTO.getPurchaseDate());
            purchase.setPurchaseDueDate(purchaseDTO.getPurchaseDueDate());
            purchase.setPurchaseCreatedAt(purchaseDTO.getPurchaseCreatedAt());
            purchase.setPurchaseUpdatedAt(LocalDate.now());
            Purchase updatepurchase = purchaseRepository.save(purchase);

            return new PurchaseDTO(  updatepurchase.getPurchaseId(),
                    updatepurchase.getUserId(),
                    updatepurchase.getPurchaseCreatedAt(),
                    updatepurchase.getPurchaseDate(),
                    updatepurchase.getPurchaseDueDate(),
                    updatepurchase.getPurchaseStatus().getLabel(),
                    updatepurchase.getPurchaseUpdatedAt());
        } else {
            throw new IllegalArgumentException("발주대기가 아니라면 수정을 못합니다.");
            //발주대기 상태가 아니라면 수정 못한다는 것을 표시
        }


    }

   @Transactional
    public boolean deletedpurchase(Integer id) {
       if (!purchaseRepository.existsById(id)) {
           throw new IllegalArgumentException("해당 발주가 없습니다. ID: " + id);
       }



       return true;
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


}
