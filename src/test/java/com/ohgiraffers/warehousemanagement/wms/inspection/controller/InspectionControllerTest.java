package com.ohgiraffers.warehousemanagement.wms.inspection.controller;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionStatus;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.service.InspectionServiceImpl;
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
@Transactional
@Rollback
class InspectionControllerTest {
    private static final Logger log = LoggerFactory.getLogger(InspectionControllerTest.class);
    private final InspectionServiceImpl inspectionService;

    @Autowired
    public InspectionControllerTest(InspectionServiceImpl inspectionService) {
        this.inspectionService = inspectionService;
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
    @DisplayName("메인 페이지 조회 테스트")
    void getAllInspections() {
        Page<Inspection> allInspection = inspectionService.getAllInspection(1, 10);

        log.info("Inspection\n{}", allInspection.getContent());

        assertTrue(allInspection.getContent().size() <= 10, "페이징한 개수는 10개보다 작아야 합니다.");
    }

    @Test
    @DisplayName("태그로 조회 테스트")
    void getAllTagInspections() {
        Page<Inspection> allTagInspection = inspectionService.getAllInspectionByTag("INSPECTION", 1, 10);

        log.info("Inspection\n{}", allTagInspection.getContent());

        assertTrue(allTagInspection.getContent().size() <= 10, "페이징한 개수는 10개보다 작아야 합니다.");
    }

    @Test
    @DisplayName("다중 등록 테스트")
    void createMultiInspection() {
        for(int i = 1; i <= 43; i++) {
            InspectionRequestDTO dto = new InspectionRequestDTO(1, null, InspectionTransactionType.INSPECTION, i, i, 0, InspectionStatus.OK);


            InspectionResponseDTO inspection = inspectionService.createInspection(dto);

            log.info(inspection.toString());

            assertNotNull(inspection, "등록에 성공해야 합니다!");
        }
    }
}