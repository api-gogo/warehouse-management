package com.ohgiraffers.warehousemanagement.wms.returning.service;


import com.ohgiraffers.warehousemanagement.wms.returning.ReturnShipmentStatus;
import com.ohgiraffers.warehousemanagement.wms.returning.ReturningShipmentCause;
import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnShipmentDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipment;
import com.ohgiraffers.warehousemanagement.wms.returning.model.entity.ReturnShipmentItem;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnShipmentItemRepository;
import com.ohgiraffers.warehousemanagement.wms.returning.model.repository.ReturnShipmentRepository;

import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesItem;
import com.ohgiraffers.warehousemanagement.wms.sales.service.SalesService;
import com.ohgiraffers.warehousemanagement.wms.shipment.service.ShipmentService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ReturnShipmentService {

    private final ReturnShipmentRepository returnShipmentRepository;
    private final ReturnShipmentItemRepository returnShipmentItemRepository;
    private final ShipmentService shipmentService;
    private final SalesService salesService;

    @Autowired
    public ReturnShipmentService(ReturnShipmentRepository returnShipmentRepository, ReturnShipmentItemRepository returnShipmentItemRepository, ShipmentService shipmentService, SalesService salesService) {
        this.returnShipmentRepository = returnShipmentRepository;
        this.returnShipmentItemRepository = returnShipmentItemRepository;
        this.shipmentService = shipmentService;
        this.salesService = salesService;
    }


    //전체조회
    public List<ReturnShipmentDTO> getAllReturns() {//엔티티 리스트로 받기

        List<ReturnShipment> findAll = returnShipmentRepository.findAllByIsDeleted(true);//활성화된 항목만 조회

        List<ReturnShipmentDTO> returnLists = new ArrayList<>();

        for (ReturnShipment rsEntity : findAll) {

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

//                List<ReturnShipmentItem> lotNumbers = new ArrayList<>();
//                List<ReturnShipmentItem> quantities = new ArrayList<>();
//                List<ReturnShipmentItem> contents = new ArrayList<>();

            returnLists.add(rsDTO);
        }

        return returnLists;
    }


    //등록
    @Transactional
    public int createReturns(ReturnShipmentDTO returnShipmentDTO) {

        /*예외처리*/
        Logger logger = LoggerFactory.getLogger(ReturnShipmentService.class);
        logger.info("Received DTO: lotNumber={}, returnShipmentContent={}",
                returnShipmentDTO.getLotNumber(),
                returnShipmentDTO.getReturnShipmentContent());

        //
        Integer salesId = shipmentService.getSaleIdByShipmentId(returnShipmentDTO.getShipmentId());
        SalesItem salesItem = salesService.getSalesItemBySalesId(salesId);
        System.out.println();

        /*유효성 검증*/
        if (returnShipmentDTO.getLotNumber() == null || returnShipmentDTO.getLotNumber().isEmpty()) {
            throw new IllegalArgumentException("로트넘버는 필수입니다.");
        }

        List<ReturningShipmentCause> causes = returnShipmentDTO.getReturnShipmentContent();
        if (causes == null || causes.isEmpty()) {
            throw new IllegalArgumentException("반품 사유는 최소 하나 이상 선택해야 합니다.");
        }

        ReturnShipment returnShipmentEntity = new ReturnShipment();

        returnShipmentEntity.setShipmentId(returnShipmentDTO.getShipmentId());
        returnShipmentEntity.setStoreId(returnShipmentDTO.getStoreId());
        returnShipmentEntity.setUserId(returnShipmentDTO.getUserId());
        returnShipmentEntity.setReturnShipmentStatus(ReturnShipmentStatus.RETURN_WAITING);
        returnShipmentEntity.setReturnShipmentCreatedAt(LocalDateTime.now());
        returnShipmentEntity.setDeleted(true);

        //메인 엔티티에 저장
        ReturnShipment savedReturns = returnShipmentRepository.save(returnShipmentEntity);
        System.out.println("서비스 savedReturns엔티티 : " + savedReturns);

        //반품서 리스트 생성
        List<ReturnShipmentItem> rsItemList  = new ArrayList<>();

        for (int i = 0; i < returnShipmentDTO.getLotNumber().size(); i++) {
            ReturnShipmentItem rsItem = new ReturnShipmentItem();
            //메인 엔티티 연결
            rsItem.setReturnShipmentId(savedReturns);

            //반품 사유설정
            if (i < returnShipmentDTO.getReturnShipmentContent().size()) {
                rsItem.setReturnShipmentContent(returnShipmentDTO.getReturnShipmentContent().get(i));
            }

            //로트 넘버 설정 (리스트 -> String)
            if (i < returnShipmentDTO.getLotNumber().size()) {
                String lotNumber = returnShipmentDTO.getLotNumber().get(i);
                rsItem.setLotNumber(lotNumber);
            }

            //DTO에서 반품수량 설정
            if (returnShipmentDTO.getReturnShipmentQuantity() != null &&
                    i < returnShipmentDTO.getReturnShipmentQuantity().size()) {
                rsItem.setReturnShipmentQuantity(returnShipmentDTO.getReturnShipmentQuantity().get(i));
            }   else {
            // 수량 정보가 없는 경우 기본값 설정 (예: 1) -->
            rsItem.setReturnShipmentQuantity(1);
            }

            // 아이템 저장 및 리스트에 추가
            rsItemList.add(rsItem);
            returnShipmentItemRepository.save(rsItem);
            System.out.println("sv rsItem엔티티 " + rsItem);
        }
        return savedReturns.getReturnShipmentId();



       // System.out.println("서비스 savedReturns엔티티 : " + savedReturns); //->테스트

//       //반품서 리스트 저장
//        List<ReturnShipmentItem> rsItemList = new ArrayList<>();
//
//        for(int i=0; i <returnShipmentDTO.getLotNumber().size(); i++) { //@@@@@@@@애매
//            ReturnShipmentItem rsItem = new ReturnShipmentItem();
//            rsItem.setReturnShipmentId(savedReturns);
//            rsItem.setReturnShipmentContent(returnShipmentDTO.getReturnShipmentContent().get(i));
//            rsItem.setReturnShipmentQuantity(Integer.valueOf(returnShipmentDTO.getLotNumber().get(i)));
//            //rsItem.setReturnShipmentQuantity(returnShipmentDTO.getLotNumber().get(i));
//            rsItem.setLotNumber(returnShipmentDTO.getLotNumber().get(i));
//
//            rsItemList.add(rsItem);
//            returnShipmentItemRepository.save(rsItem);
//            System.out.println("sv rsItem엔티티 "+rsItem); //
//        }
//        int returnShipmentId =  savedReturns.getReturnShipmentId();
//
//        return returnShipmentId;

    }



    //상세조회
    public ReturnShipmentDTO getReturnsByID(Integer returnShipmentId) {
        ReturnShipment findReturn = returnShipmentRepository.findById(returnShipmentId) //데이터가 없을 경우 추가 예외처리 ->findby반환 타입 : optional
                .orElseThrow(() -> new IllegalArgumentException("해당 반품 정보가 존재하지 않습니다. "));

        // 기본 DTO 생성
        ReturnShipmentDTO findDTO = new ReturnShipmentDTO(
                //엔티티에서 가져온 값을 저장
                findReturn.getReturnShipmentId(),
                findReturn.getStoreId(),
                findReturn.getUserId(),
                findReturn.getReturnShipmentStatus(),
                findReturn.getReturnShipmentCreatedAt(),
                findReturn.getReturnShipmentUpdatedAt(),
                findReturn.getReturnShipmentDeletedAt(),
                findReturn.isDeleted(),
                findReturn.getShipmentId()
        );

        // ReturnShipmentItem 정보 처리
        if (findReturn.getReturnShipmentItems() != null && !findReturn.getReturnShipmentItems().isEmpty()) {
            List<String> lotNumbers = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            List<ReturningShipmentCause> causes = new ArrayList<>();

            for (ReturnShipmentItem item : findReturn.getReturnShipmentItems()) {
                lotNumbers.add(item.getLotNumber());
                quantities.add(item.getReturnShipmentQuantity());
                causes.add(item.getReturnShipmentContent());

                // 디버깅 로그
                System.out.println("Item: LotNumber=" + item.getLotNumber() +
                                   ", Quantity=" + item.getReturnShipmentQuantity() +
                                   ", Cause=" + item.getReturnShipmentContent());
            }

            // DTO에 설정
            findDTO.setLotNumber(lotNumbers);
            findDTO.setReturnShipmentQuantity(quantities);
            findDTO.setReturnShipmentContent(causes);


            // 디버깅 로그
            System.out.println("DTO populated with: " +
                               "LotNumbers=" + lotNumbers +
                               ", Quantities=" + quantities +
                               ", Causes=" + causes);
            System.out.println(findDTO);
        } else {
            System.out.println("No ReturnShipmentItems found for ReturnShipment ID: " + returnShipmentId);
        }

        return findDTO;
    }

    //논리적 삭제
    public List<ReturnShipmentDTO> getActiveReturns() {
        return returnShipmentRepository.findAllByIsDeleted(true)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReturnShipmentDTO convertToDTO(ReturnShipment returnShipment) {
        ReturnShipmentDTO dto = new ReturnShipmentDTO();

        dto.setReturnShipmentId(returnShipment.getReturnShipmentId());
        dto.setStoreId(returnShipment.getStoreId());
        dto.setUserId(returnShipment.getUserId());
        dto.setReturnShipmentStatus(returnShipment.getReturnShipmentStatus());
        dto.setReturnShipmentCreatedAt(returnShipment.getReturnShipmentCreatedAt());
        dto.setReturnShipmentUpdatedAt(returnShipment.getReturnShipmentUpdatedAt());
        dto.setReturnShipmentDeletedAt(returnShipment.getReturnShipmentDeletedAt());
        dto.setDeleted(returnShipment.isDeleted());
        dto.setShipmentId(returnShipment.getShipmentId());

        return dto;
    }

    //삭제
    @Transactional
    public boolean deleteReturns(Integer returnShipmentId) { //단순 삭제, 삭제 성공 여부를 쓰는게 나을까? 성공시 t 실패시 f
        try{
            ReturnShipment returnShipment = returnShipmentRepository.findById(returnShipmentId) //ID를 이용해 조회
                    .orElseThrow(() -> new RuntimeException("삭제할 반품서가 없습니다"));

            returnShipment.setDeleted(false); //false=삭제 상태, true=활성화 상태
            returnShipment.setReturnShipmentDeletedAt(LocalDateTime.now());
            returnShipmentRepository.save(returnShipment);

            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 반품 정보 수정 메소드
     * @param returnShipmentId 수정할 반품 ID
     * @param returnShipmentDTO 수정할 반품 정보가 담긴 DTO
     * @return 수정된 반품 정보 DTO
     */
    @Transactional
    public ReturnShipmentDTO updateReturns(Integer returnShipmentId, ReturnShipmentDTO returnShipmentDTO) {
        // 로깅 설정
        Logger logger = LoggerFactory.getLogger(ReturnShipmentService.class);
        logger.info("updateReturns 시작 - ID: {}, DTO 상태: {}", returnShipmentId, returnShipmentDTO.getReturnShipmentStatus());
        
        // 1. 기존 엔티티 조회
        ReturnShipment returnShipment = returnShipmentRepository.findById(returnShipmentId)
                .orElseThrow(() -> new IllegalArgumentException("수정할 반품 데이터가 없습니다. ID: " + returnShipmentId));
        
        // 2. 기본 정보 업데이트
        // 매장 ID 설정
        returnShipment.setStoreId(returnShipmentDTO.getStoreId());
        
        // 담당자 ID 설정
        returnShipment.setUserId(returnShipmentDTO.getUserId());
        
        // 출고 ID 설정
        returnShipment.setShipmentId(returnShipmentDTO.getShipmentId());
        
        // 3. 상태 업데이트 - 명시적으로 처리 (중요)
        logger.info("상태 변경 전: {}", returnShipment.getReturnShipmentStatus());
        returnShipment.setReturnShipmentStatus(returnShipmentDTO.getReturnShipmentStatus());
        logger.info("상태 변경 후: {}", returnShipment.getReturnShipmentStatus());
        
        // 4. 수정일시 업데이트
        returnShipment.setReturnShipmentUpdatedAt(LocalDateTime.now());
        
        // 5. 기본 정보 저장
        ReturnShipment savedEntity = returnShipmentRepository.save(returnShipment);
        logger.info("기본 정보 저장 완료 - 상태: {}", savedEntity.getReturnShipmentStatus());
        
        // 6. 기존 아이템 모두 삭제
        returnShipmentItemRepository.deleteByReturnShipmentId(savedEntity);
        
        // 7. 로트 번호와 반품 사유 유효성 검사
        if (returnShipmentDTO.getLotNumber() == null || returnShipmentDTO.getLotNumber().isEmpty()) {
            throw new IllegalArgumentException("로트 번호는 최소 하나 이상 필요합니다.");
        }
        
        if (returnShipmentDTO.getReturnShipmentContent() == null || returnShipmentDTO.getReturnShipmentContent().isEmpty()) {
            throw new IllegalArgumentException("반품 사유는 최소 하나 이상 필요합니다.");
        }
        
        // 8. 새 아이템 생성 및 추가
        List<ReturnShipmentItem> newItems = new ArrayList<>();
        for (int i = 0; i < returnShipmentDTO.getLotNumber().size(); i++) {
            ReturnShipmentItem item = new ReturnShipmentItem();
            item.setReturnShipmentId(savedEntity);
            
            // 로트 번호 설정
            item.setLotNumber(returnShipmentDTO.getLotNumber().get(i));
            
            // 반품 사유 설정
            if (i < returnShipmentDTO.getReturnShipmentContent().size()) {
                item.setReturnShipmentContent(returnShipmentDTO.getReturnShipmentContent().get(i));
            } else {
                // 인덱스 범위를 벗어나면 첫 번째 사유 사용
                item.setReturnShipmentContent(returnShipmentDTO.getReturnShipmentContent().get(0));
            }
            
            // 수량 설정
            if (returnShipmentDTO.getReturnShipmentQuantity() != null && 
                i < returnShipmentDTO.getReturnShipmentQuantity().size()) {
                item.setReturnShipmentQuantity(returnShipmentDTO.getReturnShipmentQuantity().get(i));
            } else {
                // 기본값 설정
                item.setReturnShipmentQuantity(1);
            }
            
            newItems.add(item);
            logger.info("아이템 생성 - 로트번호: {}, 수량: {}, 사유: {}", 
                    item.getLotNumber(), item.getReturnShipmentQuantity(), item.getReturnShipmentContent());
        }
        
        // 9. 아이템 저장
        returnShipmentItemRepository.saveAll(newItems);
        logger.info("반품 항목 저장 완료 - 항목 수: {}", newItems.size());
        
        // 10. 저장된 엔티티를 DTO로 변환하여 반환
        // 직접 DB에서 새로 조회하여 최신 상태 반환
        ReturnShipmentDTO updatedDTO = getReturnsByID(returnShipmentId);
        logger.info("updateReturns 완료 - ID: {}, 반환 DTO 상태: {}", 
                updatedDTO.getReturnShipmentId(), updatedDTO.getReturnShipmentStatus());
        
        return updatedDTO;
    }

    public boolean updateStatusReturns(Integer returnShipmentId, ReturnShipmentStatus status) {
        ReturnShipment findReturn = returnShipmentRepository.findById(returnShipmentId).orElseThrow(
                ()-> new NullPointerException("상태를 변경할 반품 데이터가 없습니다."));

        // 모든 상태로 변경 가능하도록 제한 제거
        findReturn.setReturnShipmentStatus(status);
        findReturn.setReturnShipmentUpdatedAt(LocalDateTime.now());
        returnShipmentRepository.save(findReturn); // 저장 추가
        return true;
    }
    /*public Integer getReturnShipmentById(Integer returnShipmentId) {
        return returnShipmentRepository.findById(returnShipmentId).get().getReturnShipmentId();
    }*/
    //비즈니스 로직

}

