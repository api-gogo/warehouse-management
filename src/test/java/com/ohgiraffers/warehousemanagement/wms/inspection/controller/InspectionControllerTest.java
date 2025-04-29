package com.ohgiraffers.warehousemanagement.wms.inspection.controller;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionStatus;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.service.InspectionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InspectionControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InspectionControllerTest.class);
    private final InspectionServiceImpl inspectionService;
    private InspectionResponseDTO inspection;

    @Autowired
    public InspectionControllerTest(InspectionServiceImpl inspectionService) {
        this.inspectionService = inspectionService;
    }

    @BeforeEach
    void setUp() {
        InspectionRequestDTO dto = new InspectionRequestDTO(1, null, InspectionTransactionType.INSPECTION, 50, 50, 0, InspectionStatus.OK);
        inspection = inspectionService.createInspection(dto);
    }


    @Test
    @DisplayName("등록 테스트")
    void createInspection() {
        InspectionRequestDTO dto = new InspectionRequestDTO(1, null, InspectionTransactionType.INSPECTION, 50, 50, 0, InspectionStatus.OK);

        InspectionResponseDTO inspection = inspectionService.createInspection(dto);

        log.info(inspection.toString());

        assertNotNull(inspection, "등록에 성공해야 합니다!");
    }

    @Test
    @DisplayName("수정 테스트")
    void updateInspection() {
        InspectionRequestDTO dto = new InspectionRequestDTO(
                inspection.getUserId(), inspection.getTransactionId(),
                InspectionTransactionType.INSPECTION, inspection.getInspectionQuantity(),
                inspection.getAcceptedQuantity(), inspection.getDefectiveQuantity(),
                InspectionStatus.stringToInspectionStatus(inspection.getInspectionStatus()));
        dto.setAcceptedQuantity(49);
        dto.setDefectiveQuantity(1);
        inspectionService.updateInspection(inspection.getInspectionId(), dto);
    }

    @Test
    @DisplayName("수정 사항 없음 테스트[예외 발생]")
    void updateInspection_NotFound() {
        InspectionRequestDTO dto = new InspectionRequestDTO(
                inspection.getUserId(), inspection.getTransactionId(),
                InspectionTransactionType.INSPECTION, inspection.getInspectionQuantity(),
                inspection.getAcceptedQuantity(), inspection.getDefectiveQuantity(),
                InspectionStatus.stringToInspectionStatus(inspection.getInspectionStatus()));

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inspectionService.updateInspection(inspection.getInspectionId(), dto);
        });

        Assertions.assertEquals("변경사항이 없습니다!", exception.getMessage());
    }

    @Test
    @DisplayName("메인 페이지 조회 테스트")
    void getAllInspections() {
        Page<InspectionResponseDTO> allInspection = inspectionService.getAllInspection(1, 10);

        log.info("조회된 Inspection 목록\n{}", allInspection.getContent());

        assertTrue(allInspection.getContent().size() <= 10, "페이징한 개수는 10개보다 작아야 합니다.");
    }

    @Test
    @DisplayName("태그로 조회 테스트")
    void getAllTagInspections() {
        Page<InspectionResponseDTO> allTagInspection = inspectionService.getAllInspectionByTag("INSPECTION", 1, 10);

        log.info("조회된 Inspection 목록\n{}", allTagInspection.getContent());

        assertTrue(allTagInspection.getContent().size() <= 10, "페이징한 개수는 10개보다 작아야 합니다.");
    }

    @Test
    @DisplayName("없는 태그 조회 테스트 [예외 발생]")
    void getAllTagInspections_NotFound() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inspectionService.getAllInspectionByTag("없는태그",1, 10);
        });

        Assertions.assertEquals("존재하지 않는 유형입니다!", exception.getMessage());
    }

    @Test
    @DisplayName("단일 조회 테스트")
    void getInspectionById() {
        InspectionResponseDTO inspection = inspectionService.getInspectionById(this.inspection.getInspectionId());
        log.info("조회된 inspection : {}", inspection);

        Assertions.assertEquals(this.inspection.getInspectionQuantity(), inspection.getInspectionQuantity());
    }

    @Test
    @DisplayName("단일 조회 테스트[예외 발생]")
    void getInspectionById_NotFound() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inspectionService.getInspectionById(9999L);
        });

        Assertions.assertEquals("존재하지 않는 검수 ID입니다! \n" +
                "검색 ID : 9999", exception.getMessage());
    }

    @Test
    @DisplayName("다중 등록 테스트")
    void createMultiInspection() {
        for(int i = 1; i <= 43; i++) {
            InspectionRequestDTO dto = new InspectionRequestDTO(1, null, InspectionTransactionType.INSPECTION, i, i, 0, InspectionStatus.OK);
            if(i % 2 == 0) {
                dto.setTransactionType(InspectionTransactionType.PURCHASE);
                dto.setTransactionId(1);
            }
            if(i % 3 == 0) {
                dto.setTransactionType(InspectionTransactionType.SALES);
                dto.setTransactionId(2);
            }
            if(i % 5 == 0) {
                dto.setTransactionType(InspectionTransactionType.STORAGE);
                dto.setTransactionId(3);
            }
            if(i % 7 == 0) {
                dto.setTransactionType(InspectionTransactionType.SHIPMENT);
                dto.setTransactionId(4);
            }
            if(i % 4 == 0) {
                dto.setInspectionStatus(InspectionStatus.DEFECTIVE);
            }
            if(i % 6 == 0) {
                dto.setInspectionStatus(InspectionStatus.HOLD);
            }
            if(i % 3 == 2) {
                dto.setDefectiveQuantity(i / 4);
                dto.setAcceptedQuantity(dto.getInspectionQuantity() - (i / 4));
            }


            InspectionResponseDTO inspection = inspectionService.createInspection(dto);

            log.info(inspection.toString());

            assertNotNull(inspection, "등록에 성공해야 합니다!");
        }
    }
}