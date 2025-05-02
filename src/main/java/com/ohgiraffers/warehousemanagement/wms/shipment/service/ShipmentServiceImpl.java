package com.ohgiraffers.warehousemanagement.wms.shipment.service;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.repository.InventoryRepository;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesItem;
import com.ohgiraffers.warehousemanagement.wms.sales.service.SalesService;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.inventory.service.InventoryService;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.entity.Shipment;
import com.ohgiraffers.warehousemanagement.wms.shipment.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 출고 관련 비즈니스 로직을 구현하는 서비스 클래스
 */
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger log = LoggerFactory.getLogger(ShipmentServiceImpl.class);
    private final ShipmentRepository shipmentRepository;
    private final SalesService salesService;
    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    /**
     * 생성자를 통한 의존성 주입
     */
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, SalesService salesService,
                               InventoryService inventoryService, InventoryRepository inventoryRepository) {
        this.shipmentRepository = shipmentRepository;
        this.salesService = salesService;
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentPageResponseDTO getAllShipments(int page, int size, String status, String searchTerm) {
        log.info("출고 목록 조회 - 페이지: {}, 크기: {}, 상태: {}, 검색어: {}", page, size, status, searchTerm);
        List<Shipment> allShipments = shipmentRepository.findAll();
        List<Shipment> filteredShipments = allShipments;

        if (status != null && !status.isEmpty()) {
            filteredShipments = filteredShipments.stream()
                    .filter(shipment -> shipment.getShipmentStatus().equals(status))
                    .collect(Collectors.toList());
        }

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String searchLower = searchTerm.trim().toLowerCase();
            filteredShipments = filteredShipments.stream()
                    .filter(shipment -> String.valueOf(shipment.getShipmentId()).toLowerCase().contains(searchLower) ||
                            String.valueOf(shipment.getSaleId()).toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }

        filteredShipments.sort(Comparator.comparing(Shipment::getShipmentDate).reversed());
        int totalItems = filteredShipments.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<Shipment> pagedShipments = startIndex < totalItems ? filteredShipments.subList(startIndex, endIndex) : List.of();
        List<ShipmentResponseDTO> shipmentDTOs = pagedShipments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        int startItem = page * size + 1;
        int endItem = Math.min((page + 1) * size, totalItems);
        return new ShipmentPageResponseDTO(shipmentDTOs, page, totalPages, size, totalItems, startItem, endItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponseDTO getShipmentById(Integer shipmentId) {
        log.info("출고 ID로 조회: {}", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        return convertToResponseDTO(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public Shipment findShipmentById(Integer shipmentId) {
        if (shipmentId == null || shipmentId <= 0) {
            throw new RuntimeException("유효하지 않은 출고 ID입니다: " + shipmentId);
        }
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 출고를 찾을 수 없습니다: " + shipmentId));
    }

    @Override
    @Transactional
    public ShipmentResponseDTO createShipment(ShipmentCreateDTO createDTO) {
        log.info("새 출고 생성: {}", createDTO);
        validateShipmentStatus(createDTO.getShipmentStatus());
        Shipment shipment = convertToShipmentEntity(createDTO);
        Shipment savedShipment = shipmentRepository.save(shipment);
        log.info("출고 생성 성공: {}", savedShipment.getShipmentId());

        // 출고 대기 상태일 경우 재고 업데이트
        if ("출고대기".equals(savedShipment.getShipmentStatus())) {
            updateInventoryForPending(savedShipment);
        }

        return convertToResponseDTO(savedShipment);
    }

    @Override
    @Transactional
    public ShipmentResponseDTO updateShipment(Integer shipmentId, ShipmentCreateDTO updateDTO) {
        log.info("출고 ID: {} 업데이트, 데이터: {}", shipmentId, updateDTO);
        Shipment shipment = findShipmentById(shipmentId);
        String previousStatus = shipment.getShipmentStatus();
        validateShipmentStatus(updateDTO.getShipmentStatus());
        shipment.setSaleId(updateDTO.getSaleId());
        shipment.setUserId(updateDTO.getUserId());
        shipment.setShipmentDate(updateDTO.getShipmentDate());
        shipment.setShipmentStatus(updateDTO.getShipmentStatus());
        shipment.setShipmentReason(updateDTO.getShipmentReason());
        Shipment updatedShipment = shipmentRepository.save(shipment);
        log.info("출고 업데이트 성공: {}", updatedShipment.getShipmentId());

        // 상태 변경에 따른 재고 업데이트
        String newStatus = updatedShipment.getShipmentStatus();
        if ("출고대기".equals(newStatus) && !newStatus.equals(previousStatus)) {
            updateInventoryForPending(updatedShipment);
        } else if ("출고완료".equals(newStatus) && !newStatus.equals(previousStatus)) {
            updateInventoryForCompleted(updatedShipment);
        }

        return convertToResponseDTO(updatedShipment);
    }

    @Override
    @Transactional
    public void cancelShipment(Integer shipmentId) {
        log.info("출고 ID: {} 취소", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        if ("출고완료".equals(shipment.getShipmentStatus()) || "출고취소".equals(shipment.getShipmentStatus())) {
            throw new RuntimeException("이미 완료되거나 취소된 출고는 취소할 수 없습니다.");
        }
        shipment.setShipmentStatus("출고취소");
        shipmentRepository.save(shipment);
        log.info("출고 취소 성공: {}", shipmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getSaleIdByShipmentId(Integer shipmentId) {
        log.info("출고 ID: {}로 수주 ID 조회", shipmentId);
        Shipment shipment = findShipmentById(shipmentId);
        return shipment.getSaleId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getAllSaleIds() {
        log.info("모든 수주 ID 조회");
        return shipmentRepository.findAllSaleIds();
    }

    /**
     * 출고 대기 상태일 때 재고 업데이트 (가용재고 → 할당재고)
     */
    private void updateInventoryForPending(Shipment shipment) {
        log.info("출고 대기 상태로 재고 업데이트 - 출고 ID: {}", shipment.getShipmentId());
        Sales sales = salesService.getSalesBySalesId(shipment.getSaleId());
        if (sales == null) {
            throw new RuntimeException("수주 ID에 해당하는 데이터를 찾을 수 없습니다: " + shipment.getSaleId());
        }
        List<SalesItem> saleItems = sales.getSalesItems();
        if (saleItems == null || saleItems.isEmpty()) {
            throw new RuntimeException("수주 ID에 대한 상품 목록이 없습니다: " + shipment.getSaleId());
        }

        for (SalesItem item : saleItems) {
            Integer productId = item.getProductId();
            String lotNumber = getLotNumberFromSalesItem(item);
            Integer quantity = item.getSalesItemsQuantity();

            // findByProductProductIdAndLotNumber로 재고 조회
            Inventory inventory = inventoryRepository.findByProductProductIdAndLotNumber(productId, lotNumber)
                    .orElseThrow(() -> new RuntimeException(
                            "해당 상품과 로트 번호에 대한 재고를 찾을 수 없습니다 - 상품 ID: " + productId + ", 로트 번호: " + lotNumber));

            long availableStock = inventory.getAvailableStock();
            if (availableStock < quantity) {
                throw new RuntimeException(
                        "가용재고가 부족합니다 - 상품 ID: " + productId + ", 로트 번호: " + lotNumber +
                                ", 가용재고: " + availableStock + ", 요청 수량: " + quantity);
            }
            inventory.setAvailableStock(availableStock - quantity);
            inventory.setAllocatedStock(inventory.getAllocatedStock() + quantity);
            inventoryRepository.save(inventory);
            log.info("재고 업데이트 성공 - 상품 ID: {}, 로트 번호: {}, 가용재고: {}, 할  당재고: {}",
                    productId, lotNumber, inventory.getAvailableStock(), inventory.getAllocatedStock());
        }
    }

    /**
     * 출고 완료 상태일 때 재고 업데이트 (할당재고 제거)
     */
    private void updateInventoryForCompleted(Shipment shipment) {
        log.info("출고 완료 상태로 재고 업데이트 - 출고 ID: {}", shipment.getShipmentId());
        Sales sales = salesService.getSalesBySalesId(shipment.getSaleId());
        if (sales == null) {
            throw new RuntimeException("수주 ID에 해당하는 데이터를 찾을 수 없습니다: " + shipment.getSaleId());
        }
        List<SalesItem> saleItems = sales.getSalesItems();
        if (saleItems == null || saleItems.isEmpty()) {
            throw new RuntimeException("수주 ID에 대한 상품 목록이 없습니다: " + shipment.getSaleId());
        }

        for (SalesItem item : saleItems) {
            Integer productId = item.getProductId();
            String lotNumber = getLotNumberFromSalesItem(item);
            Integer quantity = item.getSalesItemsQuantity();

            // findByProductProductIdAndLotNumber로 재고 조회
            Inventory inventory = inventoryRepository.findByProductProductIdAndLotNumber(productId, lotNumber)
                    .orElseThrow(() -> new RuntimeException(
                            "해당 상품과 로트 번호에 대한 재고를 찾을 수 없습니다 - 상품 ID: " + productId + ", 로트 번호: " + lotNumber));

            long allocatedStock = inventory.getAllocatedStock();
            if (allocatedStock < quantity) {
                throw new RuntimeException(
                        "할당재고가 부족합니다 - 상품 ID: " + productId + ", 로트 번호: " + lotNumber +
                                ", 할당재고: " + allocatedStock + ", 요청 수량: " + quantity);
            }
            inventory.setAllocatedStock(allocatedStock - quantity);
            inventoryRepository.save(inventory);
            log.info("할당재고 제거 성공 - 상품 ID: {}, 로트 번호: {}, 할당재고: {}",
                    productId, lotNumber, inventory.getAllocatedStock());
        }
    }

    /**
     * SalesItem에서 lotNumber를 추출 (리플렉션 사용, getter 없음)
     */
    private String getLotNumberFromSalesItem(SalesItem item) {
        try {
            Field lotNumberField = SalesItem.class.getDeclaredField("lotNumber");
            lotNumberField.setAccessible(true);
            return (String) lotNumberField.get(item);
        } catch (Exception e) {
            throw new RuntimeException("SalesItem에서 lotNumber를 추출하는 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 출고 생성 DTO를 출고 엔티티로 변환
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
     */
    private void validateShipmentStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new RuntimeException("출고 상태는 필수 입력 항목입니다.");
        }
        if (status.length() > 20) {
            throw new RuntimeException("출고 상태는 20자를 초과할 수 없습니다.");
        }
        List<String> validStatuses = List.of("출고대기", "검수중", "출고완료", "출고지연", "출고취소");
        if (!validStatuses.contains(status)) {
            throw new RuntimeException("유효하지 않은 출고 상태입니다: " + status);
        }
    }
}