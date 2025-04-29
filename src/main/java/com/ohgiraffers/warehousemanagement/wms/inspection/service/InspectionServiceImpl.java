package com.ohgiraffers.warehousemanagement.wms.inspection.service;

import com.ohgiraffers.warehousemanagement.wms.inspection.model.common.InspectionTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.request.InspectionRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.dto.response.InspectionResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.inspection.model.entity.Inspection;
import com.ohgiraffers.warehousemanagement.wms.inspection.repository.InspectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InspectionServiceImpl implements InspectionService {
    private final InspectionRepository inspectionRepository;

    @Autowired
    public InspectionServiceImpl(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Override
    @Transactional
    public InspectionResponseDTO createInspection(InspectionRequestDTO requestDTO) {
        if(requestDTO.getTransactionId() != null && requestDTO.getTransactionType().equals(InspectionTransactionType.INSPECTION)) {
            Optional<Inspection> findInspection = inspectionRepository.findByTransactionTypeAndTransactionId(
                    requestDTO.getTransactionType(), requestDTO.getTransactionId()
            );
            if (findInspection.isPresent()) {
                throw new IllegalArgumentException("이미 검수했습니다.\n" +
                        "검수 유형 : " + requestDTO.getTransactionType().getTransactionType() +
                        "\n검수 ID : " + requestDTO.getTransactionId());
            }
        }

            Inspection inspection = new Inspection(requestDTO);

            Inspection saveInspection = inspectionRepository.save(inspection);

            return new InspectionResponseDTO(saveInspection);
    }

    public Page<InspectionResponseDTO> getAllInspection(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Inspection> inspectionList = inspectionRepository.findAllByOrderByInspectionIdDesc(pageable);
        return inspectionList.map(InspectionResponseDTO::new);
    }

    public Page<InspectionResponseDTO> getAllInspectionByTag(String type, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (InspectionTransactionType.typeContains(type)) {
            Page<Inspection> inspectionList = inspectionRepository.findAllByTransactionTypeOrderByInspectionIdDesc(InspectionTransactionType.valueOf(type), pageable);
            return inspectionList.map(InspectionResponseDTO::new);
        }
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

    @Override
    public InspectionResponseDTO getInspectionByTagAndTagId(String type, int typeId) {
        if (!InspectionTransactionType.typeContains(type))
            throw new IllegalArgumentException("존재하지 않는 유형입니다!");

        Optional<Inspection> findInspection = inspectionRepository.findByTransactionTypeAndTransactionId(
                InspectionTransactionType.valueOf(type), typeId);

        if (findInspection.isPresent()) {
            return new InspectionResponseDTO(findInspection.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 검수유형 ID 입니다! \n" +
                    "검수 유형 : " + type +
                    "\n검수 ID : " + typeId);
        }
    }

    @Transactional
    public InspectionResponseDTO updateInspection(int inspectionId, InspectionRequestDTO requestDTO) {
        Optional<Inspection> findInspection = inspectionRepository.findById(inspectionId);
        if (findInspection.isPresent()) {
            Inspection inspection = findInspection.get();
            Inspection dtoToInspection = new Inspection(requestDTO);

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
