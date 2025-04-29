package com.ohgiraffers.warehousemanagement.wms.returning.service;

import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.model.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipment;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipmentItem;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnShipmentItemRepository;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnShipmentRepository;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipment;
import com.ohgiraffers.warehousemanagement.wms.shipment.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ReturningService {

    private static final Logger log = LoggerFactory.getLogger(ReturningService.class);
    private final ReturnShipmentRepository returnShipmentRepository;
    private final ReturnShipmentItemRepository returnShipmentItemRepository;

    @Autowired
    public ReturningService(ReturnShipmentRepository returnShipmentRepository,ReturnShipmentItemRepository returnShipmentItemRepository) { //엔티티를 반환
        this.returnShipmentRepository = returnShipmentRepository;
        this.returnShipmentItemRepository = returnShipmentItemRepository;
    }

    //전체조회
    public List<ReturnShipmentDTO> getAllReturning() {//엔티티 리스트로 받기

            List<ReturnShipment> rss = returnShipmentRepository.findAll(); //엔티티로 받기
            List<ReturnShipmentDTO> rsDTOs = new ArrayList<>();
            for(ReturnShipment rsEntity : rss) {

                ReturnShipmentDTO rsDTO = new ReturnShipmentDTO(
                        rsEntity.getReturnShipmentId(),
                        rsEntity.getStoreId(),
                        rsEntity.getUserId(),
                        rsEntity.getReturnShipmentStatus(),
                        rsEntity.getReturnShipmentCreatedAt(),
                        rsEntity.getReturnShipmentUpdatedAt(),
                        rsEntity.getReturnShipmentDeletedAt(),
                        rsEntity.isDeleted(),
                        rsEntity.getShipmentId()
                );
                    rsDTOs.add(rsDTO);
            }
            return rsDTOs;
    }


    //등록 ---> 고치기@@@@@@@@@@@@가장먼저
    @Transactional
    public ReturnShipmentDTO createReturning(ReturnShipmentDTO returnShipmentDTO) {

        ReturnShipmentItem returnShipmentItem = returnShipmentItemRepository.findById(returnShipmentDTO.getReturnShipmentId())
                .orElseThrow(()-> new RuntimeException("출고번호를 찾을 수 없습니다."));
                /* set으로 세팅해주기 - DTO에 있는 값들을 엔티티로 올려야됨 */

            ReturnShipment returnShipment = new ReturnShipment();



           // returnShipment.setReturnShipmentCreatedAt(LocalDateTime.now());
            returnShipmentRepository.save(returnShipment);
            return returnShipmentDTO;
    }



    //상세조회
    public ReturnShipmentDTO getReturningById(Integer returnShipmentId) {
        ReturnShipment returnEntity = returnShipmentRepository.findById(returnShipmentId) //데이터가 없을 경우 추가 예외처리 ->findby반환 타입 : optional
                .orElseThrow(() -> new IllegalArgumentException("해당 반품 정보가 존재하지 않습니다. "));

        //List<Integer> 

        ReturnShipmentDTO findDTO = new ReturnShipmentDTO(
        //엔티티에서 가져온 값을 저장
                returnEntity.getReturnShipmentId(),
                returnEntity.getStoreId(),
                returnEntity.getUserId(),
                returnEntity.getReturnShipmentStatus(),
                returnEntity.getReturnShipmentCreatedAt(),
                returnEntity.getReturnShipmentUpdatedAt(),
                returnEntity.getReturnShipmentDeletedAt(),
                returnEntity.isDeleted(),
                returnEntity.getShipmentId()
        );

        return findDTO;
    }

    //삭제
    @Transactional
    public boolean deleteReturning(Integer returnShipmentId) { //단순 삭제, 삭제 성공 여부를 쓰는게 나을까? 성공시 t 실패시 f
        try{
            ReturnShipment returnShipment = returnShipmentRepository.findById(returnShipmentId) //ID를 이용해 조회
                    .orElseThrow(() -> new RuntimeException("삭제할 반품서가 없습니다"));

            returnShipment.setDeleted(false); //삭제 상태 -> 0: 삭제, 1: 활성화 @@@@@byte로? ,returnShipment.setDeleted(0);
            returnShipment.setReturnShipmentDeletedAt(LocalDateTime.now());
            returnShipmentRepository.save(returnShipment);

            return true;
        }catch(Exception e){
            log.error("삭제 실패",e);
            return false;
        }
    }

    //수정 - DTO를 엔티티로 바꿔서 DB에 저장
    @Transactional
    public boolean updateReturning(ReturnShipmentDTO returnShipmentDTO) {

        //DB에 해당 아이디가 존재하지 않을 시 (findby로 인해 반환값이 옵셔널, 옵셔널 객체가 빈 값일 시 ->  false 반환)
        Optional<ReturnShipment> existReturnOpt = returnShipmentRepository.findById(returnShipmentDTO.getReturnShipmentId());
        if(existReturnOpt.isEmpty()) {
            return false;
        }

        //Shipments shipments = shipmentsRepository.findById(shipmentId).orElseThrow(); -> Shipment 엔티티에서 id를 받아오기
        ReturnShipment existReturn = existReturnOpt.get(); //옵셔널 객체가 빈값이 아니라면 옵셔널 안에 든 값을 추출

        existReturn.setStoreId(returnShipmentDTO.getStoreId());
        existReturn.setUserId(returnShipmentDTO.getUserId());
        existReturn.setLotNumber(returnShipmentDTO.getLotNumber());
        existReturn.setReturnShipmentQuantity(returnShipmentDTO.getReturnShipmentQuantity());
        existReturn.setReturnShipmentContent(returnShipmentDTO.getReturnShipmentContent());
        existReturn.setReturnShipmentStatus(ReturnShipmentStatus.valueOf(returnShipmentDTO.getReturnShipmentStatus()));
        existReturn.setReturnShipmentCreatedAt(returnShipmentDTO.getReturnShipmentCreatedAt());
        existReturn.setReturnShipmentDeletedAt(returnShipmentDTO.getReturnShipmentDeletedAt());

        existReturn.setReturnShipmentUpdatedAt(LocalDateTime.now()); //수정일은 현재시간으로

        returnShipmentRepository.save(existReturn);

        return true;

    }
    //비즈니스 로직

}
