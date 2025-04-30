package com.ohgiraffers.warehousemanagement.wms.shipment.service;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipment;
import com.ohgiraffers.warehousemanagement.wms.shipment.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 출고(Shipment) 관련 비즈니스 로직을 처리하는 서비스 구현 클래스
 * - 출고 정보의 CRUD 작업 및 비즈니스 규칙을 구현
 * - 트랜잭션 관리 및 데이터 검증 수행
 */
@Service
public class ShipmentServiceImpl implements ShipmentService {

    // 로깅을 위한 Logger 객체 초기화
    private static final Logger log = LoggerFactory.getLogger(ShipmentServiceImpl.class);

    // 출고 데이터 저장소 의존성 주입
    private final ShipmentRepository shipmentRepository;

    /**
     * 생성자를 통한 의존성 주입
     * @param shipmentRepository 출고 데이터 접근을 위한 저장소
     */
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * 출고 목록을 조회하고 페이징, 필터링, 검색 기능을 제공
     * @param page 요청 페이지 번호
     * @param size 페이지당 항목 수
     * @param status 출고 상태 필터 (선택 사항)
     * @param searchTerm 검색어 (선택 사항)
     * @return 페이징 정보가 포함된 출고 목록 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public ShipmentPageResponseDTO getAllShipments(int page, int size, String status, String searchTerm) {
        log.info("Fetching shipments - Page: {}, Size: {}, Status: {}, SearchTerm: {}", page, size, status, searchTerm);

        // 모든 출고 내역 조회
        List<Shipment> allShipments = shipmentRepository.findAll();

        // 상태에 따른 필터링
        List<Shipment> filteredShipments = allShipments;
        if (status != null && !status.isEmpty()) {
            filteredShipments = filteredShipments.stream()
                    .filter(shipment -> shipment.getShipmentStatus().equals(status))
                    .collect(Collectors.toList());
        }

        // 검색어에 따른 필터링 (출고 ID 또는 주문 ID 기준)
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String searchLower = searchTerm.trim().toLowerCase();
            filteredShipments = filteredShipments.stream()
                    .filter(shipment -> String.valueOf(shipment.getShipmentId()).toLowerCase().contains(searchLower) ||
                            String.valueOf(shipment.getSaleId()).toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        // 출고일 기준 내림차순 정렬 (최신 출고가 먼저 표시)
        filteredShipments.sort(Comparator.comparing(Shipment::getShipmentDate).reversed());

        // 페이징 처리
        int totalItems = filteredShipments.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<Shipment> pagedShipments = startIndex < totalItems ? filteredShipments.subList(startIndex, endIndex) : List.of();

        // 엔티티를 DTO로 변환
        List<ShipmentResponseDTO> shipmentDTOs = pagedShipments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        // 페이지 정보 계산
        int startItem = page * size + 1;
        int endItem = Math.min((page + 1) * size, totalItems);

        // 페이징 응답 DTO 생성 및 반환
        return new ShipmentPageResponseDTO(shipmentDTOs, page, totalPages, size, totalItems, startItem, endItem);
    }

    /**
     * 특정 ID의 출고 정보를 조회
     * 
     * @param shipmentId 조회할 출고 ID
     * @return 출고 정보 응답 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public ShipmentResponseDTO getShipmentById(Integer shipmentId) {
        log.info("Fetching shipment by ID: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        return convertToResponseDTO(shipment);
    }

    /**
     * 특정 ID의 출고 엔티티를 조회
     * 
     * @param shipmentId 조회할 출고 ID
     * @return 출고 엔티티
     * @throws RuntimeException 출고를 찾을 수 없거나 ID가 유효하지 않은 경우
     */
    @Override
    @Transactional(readOnly = true)
    public Shipment findShipmentById(Integer shipmentId) {
        if (shipmentId == null || shipmentId <= 0) {
            throw new RuntimeException("유효하지 않은 출고 ID입니다: " + shipmentId);
        }
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 출고를 찾을 수 없습니다: " + shipmentId));
    }

    /**
     * 새 출고 정보 생성
     * 
     * @param createDTO 생성할 출고 정보
     * @return 생성된 출고 정보 응답 DTO
     * @throws RuntimeException 출고 상태가 유효하지 않은 경우
     */
    @Override
    @Transactional
    public ShipmentResponseDTO createShipment(ShipmentCreateDTO createDTO) {
        log.info("Creating new shipment: {}", createDTO);
        // 출고 상태 유효성 검사
        validateShipmentStatus(createDTO.getShipmentStatus());

        // DTO를 엔티티로 변환하고 저장
        Shipment shipment = convertToShipmentEntity(createDTO);
        Shipment savedShipment = shipmentRepository.save(shipment);
        log.info("Shipment created successfully: {}", savedShipment.getShipmentId());
        return convertToResponseDTO(savedShipment);
    }

    /**
     * 기존 출고 정보 수정
     * 
     * @param shipmentId 수정할 출고 ID
     * @param updateDTO 수정할 출고 정보
     * @return 수정된 출고 정보 응답 DTO
     * @throws RuntimeException 출고를 찾을 수 없거나 출고 상태가 유효하지 않은 경우
     */
    @Override
    @Transactional
    public ShipmentResponseDTO updateShipment(Integer shipmentId, ShipmentCreateDTO updateDTO) {
        log.info("Updating shipment ID: {} with data: {}", shipmentId, updateDTO);
        // 기존 출고 조회 및 상태 유효성 검사
        Shipment shipment = findShipmentById(shipmentId);
        validateShipmentStatus(updateDTO.getShipmentStatus());

        // 엔티티 필드 업데이트
        shipment.setSaleId(updateDTO.getSaleId());
        shipment.setUserId(updateDTO.getUserId());
        shipment.setShipmentDate(updateDTO.getShipmentDate());
        shipment.setShipmentStatus(updateDTO.getShipmentStatus());
        shipment.setShipmentReason(updateDTO.getShipmentReason());

        // 업데이트된 엔티티 저장 및 DTO 변환하여 반환
        Shipment updatedShipment = shipmentRepository.save(shipment);
        log.info("Shipment updated successfully: {}", updatedShipment.getShipmentId());
        return convertToResponseDTO(updatedShipment);
    }

    /**
     * 출고 취소 처리
     * 
     * @param shipmentId 취소할 출고 ID
     * @throws RuntimeException 출고를 찾을 수 없거나 이미 완료/취소된 출고인 경우
     */
    @Override
    @Transactional
    public void cancelShipment(Integer shipmentId) {
        log.info("Cancelling shipment ID: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        
        // 이미 완료되거나 취소된 출고는 취소할 수 없음
        if ("출고완료".equals(shipment.getShipmentStatus()) || "출고취소".equals(shipment.getShipmentStatus())) {
            throw new RuntimeException("이미 완료되거나 취소된 출고는 취소할 수 없습니다.");
        }
        
        // 출고 상태를 '출고취소'로 변경하고 저장
        shipment.setShipmentStatus("출고취소");
        shipmentRepository.save(shipment);
        log.info("Shipment cancelled successfully: {}", shipmentId);
    }

    /**
     * 출고 ID로 주문 ID 조회
     * 
     * @param shipmentId 출고 ID
     * @return 주문 ID
     * @throws RuntimeException 출고를 찾을 수 없는 경우
     */
    @Override
    @Transactional(readOnly = true)
    public Integer getSaleIdByShipmentId(Integer shipmentId) {
        log.info("Fetching saleId for shipment ID: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        return shipment.getSaleId();
    }

    /**
     * 출고 생성 DTO를 출고 엔티티로 변환
     * 
     * @param dto 출고 생성 DTO
     * @return 변환된 출고 엔티티
     */
    private Shipment convertToShipmentEntity(ShipmentCreateDTO dto) {
        Shipment shipment = new Shipment();
        shipment.setSaleId(dto.getSaleId());
        shipment.setUserId(dto.getUserId());
        shipment.setShipmentDate(dto.getShipmentDate());
        shipment.setShipmentStatus(dto.getShipmentStatus());
        shipment.setShipmentReason(dto.getShipmentReason());
        return shipment;
    }

    /**
     * 출고 엔티티를 출고 응답 DTO로 변환
     * 
     * @param shipment 출고 엔티티
     * @return 변환된 출고 응답 DTO
     */
    private ShipmentResponseDTO convertToResponseDTO(Shipment shipment) {
        ShipmentResponseDTO dto = new ShipmentResponseDTO();
        dto.setShipmentId(shipment.getShipmentId());
        dto.setSaleId(shipment.getSaleId());
        dto.setUserId(shipment.getUserId());
        dto.setShipmentDate(shipment.getShipmentDate());
        dto.setShipmentStatus(shipment.getShipmentStatus());
        dto.setShipmentReason(shipment.getShipmentReason());
        dto.setUpdatedAt(shipment.getShipmentUpdatedAt());
        dto.setCreatedAt(shipment.getShipmentCreatedAt());
        return dto;
    }

    /**
     * 출고 상태 유효성 검증
     * 
     * @param status 검증할 출고 상태
     * @throws RuntimeException 상태가 null, 빈 문자열, 길이 초과 또는 허용되지 않는 값인 경우
     */
    private void validateShipmentStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new RuntimeException("출고 상태는 필수 입력 항목입니다.");
        }
        if (status.length() > 20) {
            throw new RuntimeException("출고 상태는 20자를 초과할 수 없습니다.");
        }
        
        // 허용된 출고 상태 목록
        List<String> validStatuses = List.of("출고대기", "검수중", "출고완료", "출고지연", "출고취소");
        if (!validStatuses.contains(status)) {
            throw new RuntimeException("유효하지 않은 출고 상태입니다: " + status);
        }
    }
}