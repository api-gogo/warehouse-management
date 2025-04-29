package com.ohgiraffers.warehousemanagement.wms.inventory.service;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.repository.InventoryRepository;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductService productService;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, ProductService productService) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
    }


    // Entity를 DTO로 변환
    public InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO result = new InventoryDTO(
                inventory.getInventoryId(),
                inventory.getStorageId(),
                inventory.getProduct().getProductId().longValue(), // Product 객체에서 ID를 추출
                inventory.getLotNumber(),
                inventory.getLocationCode(),
                inventory.getAvailableStock(),
                inventory.getAllocatedStock(),
                inventory.getDisposedStock(),
                inventory.getInventoryExpiryDate(),
                inventory.getInventoryCreatedAt(),
                inventory.getInventoryUpdatedAt()
        );
        return result;
    }


    public List<InventoryDTO> findAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 실제 존재하는 상품 ID 여부 확인
    public List<InventoryDTO> findAllInventoriesByProductId(Long productId) {
        Optional<Inventory> inventory = inventoryRepository.findByProductProductId(productId);
        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("해당 상품의 재고가 존재하지 않습니다.");
        } else {
            return inventory.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public InventoryDTO findInventoryById(Long inventoryId) {
        Inventory findInventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new IllegalArgumentException("없는 재고입니다."));
        return convertToDTO(findInventory);
    }

    public Inventory findTopByProductIdOrderByInventoryExpiryDateAsc(Integer productId){
        Optional<Inventory> findInventory = inventoryRepository.findTopByProductProductIdOrderByInventoryExpiryDateAsc(productId);

        return findInventory.get();
    };




    @Transactional
    public void updateInventory(Long inventoryId, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() ->
             new RuntimeException("없는 재고 데이터입니다."));

        inventory.setInventoryId(inventoryDTO.getInventoryId());
        inventory.setStorageId(inventoryDTO.getStorageId());
        
        // Product 객체를 찾아서 설정
        Product product = productService.findProductById(inventoryDTO.getProductId().intValue());
        inventory.setProduct(product);
        
        inventory.setLotNumber(inventoryDTO.getLotNumber());
        inventory.setLocationCode(inventoryDTO.getLocationCode());
        inventory.setAvailableStock(inventoryDTO.getAvailableStock());
        inventory.setAllocatedStock(inventoryDTO.getAllocatedStock());
        inventory.setDisposedStock(inventoryDTO.getDisposedStock());
        inventory.setInventoryExpiryDate(inventoryDTO.getInventoryExpiryDate());
        // 생성일자는 변경하지 않음 (최초 생성 시점으로 유지)
        // 수정일자는 현재 시간으로 업데이트
        inventory.setInventoryUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void deleteInventory(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    @Transactional
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {

        // 현재 시간 설정
        LocalDateTime now = LocalDateTime.now();

        // ID를 제외하고 엔티티 생성 (setter 사용)
        Inventory inventory = new Inventory();
        inventory.setStorageId(inventoryDTO.getStorageId());
        
        // Product 객체를 찾아서 설정
        Product product = productService.findProductById(inventoryDTO.getProductId().intValue());
        inventory.setProduct(product);
        
        inventory.setLotNumber(createRotNum(product));
        inventory.setLocationCode(inventoryDTO.getLocationCode());
        inventory.setAvailableStock(inventoryDTO.getAvailableStock());
        inventory.setAllocatedStock(inventoryDTO.getAllocatedStock());
        inventory.setDisposedStock(inventoryDTO.getDisposedStock());
        inventory.setInventoryExpiryDate(inventoryDTO.getInventoryExpiryDate());

        // 생성 시간과 수정 시간을 현재 시간으로 설정
        inventory.setInventoryCreatedAt(now);
        inventory.setInventoryUpdatedAt(now);

        // 저장하고 저장된 엔티티 반환
        Inventory savedInventory = inventoryRepository.save(inventory);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return convertToDTO(savedInventory);
    }

    public String createRotNum(Product product) {
        int categoryId = product.getCategory().getCategoryId();

        // 현재 날짜의 월과 일 가져오기
        LocalDate today = LocalDate.now();
        int year = today.getYear() % 100;
        int month = today.getMonthValue();

        // 일련번호 생성
        int sequence = getNextSequenceForProductToday(product.getProductId());

        // 로트 번호 생성: 카테고리ID(2자리) + 년(2자리) + 월(2자리) + 일련번호(3자리)
        String lotNumber = String.format("C%02d%02d%02d%03d", categoryId, year, month, sequence);

        return lotNumber;
    }

    public int getNextSequenceForProductToday(Integer productId) {
        // 당일 날짜 접두사 생성
        LocalDate today = LocalDate.now();
        String datePrefix = String.format("C%%__%02d%02d%%", today.getYear() % 100, today.getMonthValue()); // 예: "C%__2504%"

        // 최대 lot_number 조회
        String maxLotNumber = inventoryRepository.findMaxLotNumberByProductAndDate(productId, datePrefix);

        if (maxLotNumber == null) {
            return 1; // 첫 번째 일련번호
        }

        // lot_number에서 일련번호 추출 (마지막 3자리)
        String sequencePart = maxLotNumber.substring(maxLotNumber.length() - 3);
        return Integer.parseInt(sequencePart) + 1; // 다음 일련번호
    }

}
