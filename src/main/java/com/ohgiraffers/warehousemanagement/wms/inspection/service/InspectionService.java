package com.ohgiraffers.warehousemanagement.wms.inspection.service;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.repository.InspectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InspectionService {
    private final InspectionRepository inspectionRepository;

    @Autowired
    public InspectionService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Transactional
    public InspectionResponseDTO createInspection(InspectionRequestDTO requestDTO) {
        Optional<Inspection> findInspection = inspectionRepository.findByTransactionTypeAndTransactionId(
                requestDTO.getTransactionType(), requestDTO.getTransactionId()
        );
        if (findInspection.isPresent()) {
            throw new IllegalArgumentException("이미 검수했습니다.\n" +
                    "검수 유형 : " +requestDTO.getTransactionType().getTransactionType() +
                    "\n검수 ID : " + requestDTO.getTransactionId());
        }

        Inspection inspection = new Inspection(
                requestDTO.getUserId(), requestDTO.getTransactionId(),
                requestDTO.getTransactionType(), requestDTO.getInspectionQuantity(), requestDTO.getAcceptedQuantity(),
                requestDTO.getDefectiveQuantity(), requestDTO.getInspectionStatus()
        );

        Inspection saveInspection = inspectionRepository.save(inspection);

        return new InspectionResponseDTO(saveInspection);
    }

    public List<Inspection> getAllInspection() {
        return inspectionRepository.findAll();
    }

    public List<Inspection> getAllInspectionByTag(String type) {
        if (InspectionTransactionType.typeContains(type))
            return inspectionRepository.findAllByTransactionType(InspectionTransactionType.valueOf(type));
        else
            throw new IllegalArgumentException("존재하지 않는 유형입니다!");
    }

    public InspectionResponseDTO getInspectionById(int inspectionId) {
        Optional<Inspection> findInspection = inspectionRepository.findById(inspectionId);
        if (findInspection.isPresent()) {
            return new InspectionResponseDTO(findInspection.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 검수 ID입니다! \n" +
            "검색 ID : " + inspectionId);
        }
    }

    @Transactional
    public InspectionResponseDTO updateInspection(int inspectionId, InspectionRequestDTO requestDTO) {
        Optional<Inspection> findInspection = inspectionRepository.findById(inspectionId);
        if (findInspection.isPresent()) {
            Inspection inspection = findInspection.get();
            Inspection dtoToInspection = new Inspection(
                    requestDTO.getUserId(), requestDTO.getTransactionId(),
                    requestDTO.getTransactionType(), requestDTO.getInspectionQuantity(), requestDTO.getAcceptedQuantity(),
                    requestDTO.getDefectiveQuantity(), requestDTO.getInspectionStatus()
            );
            dtoToInspection.setInspectionId(inspection.getInspectionId());
            dtoToInspection.setInspectionDate(inspection.getInspectionDate());
            dtoToInspection.setInspectionUpdatedAt(inspection.getInspectionUpdatedAt());

            if(inspection.equals(dtoToInspection))
                throw new IllegalArgumentException("변경사항이 없습니다!");
            else {
                dtoToInspection.setInspectionUpdatedAt(LocalDateTime.now());
                Inspection saveInspection = inspectionRepository.save(dtoToInspection);
                return new InspectionResponseDTO(saveInspection);
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 검수 ID입니다! \n" +
                    "검색 ID : " + inspectionId);
        }
    }
}
