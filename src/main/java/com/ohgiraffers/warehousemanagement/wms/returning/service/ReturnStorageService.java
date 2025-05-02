package com.ohgiraffers.warehousemanagement.wms.returning.service;

import com.ohgiraffers.warehousemanagement.wms.returning.ReturningStorageCause;
import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnStorageDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnStorage;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnStorageItem;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnStorageItemRepository;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnStorageRepository;

import com.ohgiraffers.warehousemanagement.wms.storage.model.repository.StorageRepository;
import com.ohgiraffers.warehousemanagement.wms.storage.service.StorageService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReturnStorageService {


    private final ReturnStorageRepository returnStorageRepository;
    private final ReturnStorageItemRepository returnStorageItemRepository;
    private final StorageService storageService;

    @Autowired
    public ReturnStorageService(ReturnStorageRepository returnStorageRepository, ReturnStorageItemRepository returnStorageItemRepository, StorageService storageService) {
        this.returnStorageRepository = returnStorageRepository;
        this.returnStorageItemRepository = returnStorageItemRepository;
        this.storageService = storageService;
    }

    /*전체 조회*/
    public List<ReturnStorageDTO> getAllReturns() {
        List<ReturnStorage> findAll = returnStorageRepository.findAll();
        List<ReturnStorageDTO> returnLists = new ArrayList<>();

        for (ReturnStorage storageEntity : findAll) {

            ReturnStorageDTO storageDTO = new ReturnStorageDTO(
                    storageEntity.getReturnStorageId(),
                    storageEntity.getUserId(),
                    storageEntity.getReturnStoragesCreatedAt(),
                    storageEntity.getStorageId()

            );
            returnLists.add(storageDTO);
        }
        return returnLists;

    }

    /*등록*/
    @Transactional
    public int createReturns(ReturnStorageDTO returnStorageDTO) {

        /*예외처리*/
        Logger logger = LoggerFactory.getLogger(ReturnShipmentService.class);
        logger.info("Recived DTO : returnShipmentContent={}, returnShipmentQuantity={}",
                returnStorageDTO.getReturnStorageContent(),
                returnStorageDTO.getReturnStorageQuantity());

        /*유효성 검증*/
        if (returnStorageDTO.getReturnStorageQuantity() == null || returnStorageDTO.getReturnStorageQuantity().isEmpty()) {
            throw new IllegalArgumentException("수량은 필수입니다.");
        }


        List<ReturningStorageCause> causes = returnStorageDTO.getReturnStorageContent();
        if (causes == null || causes.isEmpty()) {
            throw new IllegalArgumentException("반품 사유는 최소 하나 이상 선택해야 합니다.");
        }

        // 입고 반품서 등록
        ReturnStorage returnStorageEntity = new ReturnStorage(
                returnStorageDTO.getStorageId(),
                returnStorageDTO.getUserId(),
                LocalDateTime.now()
        );
        System.out.println("returnstorage생성");
        System.out.println(returnStorageDTO);

//        storageEntity.setUserId(returnStorageDTO.getUserId());
//        storageEntity.setStorageId(returnStorageDTO.getStorageId());
//        storageEntity.setReturnStoragesCreatedAt(LocalDateTime.now());

        ReturnStorage savedStorage = returnStorageRepository.save(returnStorageEntity);
        System.out.println("savedStorage : "+savedStorage); // 입고반품서

        //로트넘버 없이 멀로 리스트를 만들까.. -> storage에서 purchaseId로 조회
        List<ReturnStorageItem> storageItemsList = new ArrayList<>();

        //
//        for(int i=0; i <storageService. )



        return 0;
    }


    /*상세조회*/
    public ReturnStorageDTO getReturnsById(Integer returnStorageId) {
        ReturnStorage findStorage = returnStorageRepository.findById(returnStorageId)
                .orElseThrow(() -> new NullPointerException("해당 반품 정보가 존재하지 않습니다. "));

        ReturnStorageDTO findDTO = new ReturnStorageDTO(
                findStorage.getReturnStorageId(),
                findStorage.getUserId(),
                findStorage.getReturnStoragesCreatedAt(),
                findStorage.getReturnStorageId()
        );

        if (findStorage.getReturnStorageItems() != null && !findStorage.getReturnStorageItems().isEmpty()) {
            List<ReturningStorageCause> returnStorageContent = new ArrayList<>();
            List<Integer> returnStorageQuantity = new ArrayList<>();

            for (ReturnStorageItem item : findStorage.getReturnStorageItems()) {
               returnStorageContent.add(item.getReturnStorageContent());
               returnStorageQuantity.add(item.getReturnStorageQuantity());
                /*디버깅 로그*/
                System.out.println("Item: Cause=" + item.getReturnStorageContent() +
                        ", Quantity=" + item.getReturnStorageQuantity());

            }
            findDTO.setReturnStorageContent(returnStorageContent);
            findDTO.setReturnStorageQuantity(returnStorageQuantity);

            //디버깅 로그
            System.out.println("DTO populated with: " +
                    "Cause=" + returnStorageContent.size() +
                    ", Quantity=" + returnStorageQuantity.size());
        } else {
            System.out.println("No ReturnShipmentItems found for ReturnShipment ID: " + returnStorageId);
        }
        return findDTO;
    }

//    /*삭제*/
//    @Transactional
//    public boolean deleteReturns(Integer returnStorageId) {
//        try{
//            ReturnStorage returnStorage = returnStorageRepository.findById(returnStorageId)
//                    .orElseThrow(() -> new RuntimeException("삭제할 반품서가 없습니다."));
//            returnStorage.
//        }
//    }




}