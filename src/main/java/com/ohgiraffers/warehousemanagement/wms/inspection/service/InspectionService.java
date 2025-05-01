package com.ohgiraffers.warehousemanagement.wms.inspection.service;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface InspectionService {
    InspectionResponseDTO createInspection(InspectionRequestDTO requestDTO);

    InspectionResponseDTO getInspectionByTagAndTagId(String type, Long typeId);
}
