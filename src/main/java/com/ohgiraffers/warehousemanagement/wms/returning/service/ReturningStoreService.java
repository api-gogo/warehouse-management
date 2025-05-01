package com.ohgiraffers.warehousemanagement.wms.returning.service;

import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnStorageDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnStorage;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.Storages;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnStorageRepository;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.StorageRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReturningStoreService {

    private static final Logger logger = LoggerFactory.getLogger(ReturningStoreService.class);
    private final ReturnStorageRepository returnStorageRepository;
    //private final ShipmentRepository shipmentRepository;
    private final StorageRepository storageRepository; // 생성에서 필요함

    @Autowired
    public ReturningStoreService(ReturnStorageRepository returnStorageRepository) {
        this.returnStorageRepository = returnStorageRepository;
        //this.shipmentRepository = shipmentsRepository;
        this.storageRepository = storageRepository;
    }
//전체조회 -> PK값 있어야됨
    public List<ReturnStorageDTO> getALlReturning() {
        List<ReturnStorage> rs = returnStorageRepository.findAll();
        List<ReturnStorageDTO> rsDTOs = new ArrayList<>();
        for (ReturnStorage rsEntity : rs) {

          // Integer StorageId = rsEntity.getStorageId().getStorageId() -> .getStorage() 외래키 참조 횟수

            ReturnStorageDTO rsDTO = new ReturnStorageDTO(
                    rsEntity.getReturnStorageId(),
                    rsEntity.getUserId(),
                    rsEntity.getReturnStoragesContent(),
                    rsEntity.getReturnStoragesCreatedAt(),
                    rsEntity.getStorageId() //나중에 외래키로 바꿔줘야됨
            );
                rsDTOs.add(rsDTO);
        }
        return rsDTOs;

    }

    //등록 -> PK 값 필요x --> 고치기 외래키 써야됨
    @Transactional
    public ReturnStorageDTO CreateReturning(ReturnStorageDTO returnStorageDTO) {
        /*나중에 입고의 엔티티 클래스로 바꿔줘야됨@@@@@@*/
        Storages storage = storageRepository.findById(returnStorageDTO.getStorageId())
                .orElseThrow(() -> new RuntimeException("No such storage found")); //-> 고치기 반품도


        ReturnStorage returnStorage = new ReturnStorage(returnStorageDTO.getStorageId(),
                                        returnStorageDTO.getUserId(),
                                        /*returnStorageDTO.getReturnStorageCreatedAt()*/
                                        LocalDateTime.now(), //등록일
                                        returnStorageDTO.getReturnStorageContent()
                                        );

        returnStorageRepository.save(returnStorage);
        return returnStorageDTO;
    }
    //상세 조회 -> PK값 필요
    public ReturnStorageDTO getReturningById(Integer returnStorageId) {

        ReturnStorage rsEntity = returnStorageRepository.findById(returnStorageId)
        .orElseThrow(() -> new RuntimeException("해당 반품 정보가 존재하지 않습니다."));

        ReturnStorageDTO rsDTO = new ReturnStorageDTO();
        rsDTO.setReturnStorageId(rsEntity.getReturnStorageId());//PK 값
        rsDTO.setUserId(rsEntity.getUserId());
        rsDTO.setReturnStorageContent(rsEntity.getReturnStoragesContent());
        rsDTO.setReturnStorageCreatedAt(rsEntity.getReturnStoragesCreatedAt());
        rsDTO.setStorageId(rsEntity.getStorageId());

        return rsDTO;
    }


    //삭제->삭제일 등록
    @Transactional
    public boolean deleteReturning(Integer returnStorageId) {
        try{
            ReturnStorage returnStorage = returnStorageRepository.findById(returnStorageId)
                    .orElseThrow(() -> new RuntimeException("삭제할 반품서가 없습니다"));
            returnStorage.setReturnStoragesCreatedAt(LocalDateTime.now());
            returnStorageRepository.save(returnStorage);

            return true;
        }catch (Exception e) {
            logger.error("삭제 실패", e);
            return false;
        }
    }

    @Transactional
    public boolean updateReturning(@Valid ReturnStorageDTO returnStorageDTO) {

        Optional<ReturnStorage> existReturnOpt = returnStorageRepository.findById(returnStorageDTO.getReturnStorageId());
        if(existReturnOpt.isEmpty()) {
            return false;
        }

        ReturnStorage existReturn = existReturnOpt.get();

        existReturn.setReturnStoragesCreatedAt(LocalDateTime.now());

        returnStorageRepository.save(existReturn);

        return true;
    }
    //수정->수정일
}
