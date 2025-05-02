package com.ohgiraffers.warehousemanagement.wms.inventory.service;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.InventoryLogTransactionType;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.InventoryLog;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.repository.InventoryLogRepository;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.repository.InventoryRepository;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.product.model.repository.ProductRepository;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseItem;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.repository.PurchaseItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class InventoryServicelmpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ProductRepository productRepository;


    public InventoryServicelmpl(InventoryRepository inventoryRepository, InventoryLogRepository inventoryLogRepository, PurchaseItemRepository purchaseItemRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryLogRepository = inventoryLogRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.productRepository = productRepository;
    }

    // Entity를 InventoryDTO로 변환
    public InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO result = new InventoryDTO(
                inventory.getInventoryId(),
                inventory.getStorageId(),
                inventory.getProduct().getProductId().longValue(), // Product 객체에서 ID를 추출
                inventory.getProduct().getProductName(),
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


    public InventoryDTO findInventoryById(Long inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        if (inventory.isEmpty()) {
            throw new IllegalArgumentException("해당 재고가 존재하지 않습니다.");
        } else {
            return convertToDTO(inventory.get());
        }
    }

    public Inventory findTopByProductIdOrderByInventoryExpiryDateAsc(Integer productId) {
        Optional<Inventory> findInventory = inventoryRepository.findTopByProductProductIdOrderByInventoryExpiryDateAsc(productId);
        if (findInventory.isPresent()) {
            return findInventory.get();
        } else {
            throw new IllegalArgumentException("해당 재고 아이디의 재고가 없습니다. \n" +
                    "재고 ID : " + productId);
        }
    }

    ;

    // 제품 ID에 해당하는 전체 재고 조회 (유통기한 내림차순)
    public Page<InventoryDTO> findByProductProductIdOrderByInventoryExpiryDateAsc(long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> inventory = inventoryRepository.findByProductProductIdOrderByInventoryExpiryDateAsc(productId, pageable);

        if (inventory.isEmpty()) {
            throw new IllegalArgumentException(productId + "에 해당하는 재고가 존재하지 않습니다.");
        } else {
            return inventory.map(inventory1 -> convertToDTO(inventory1));
        }
    }

    // 페이징네이션 구현 - 전체 목록 조회
    public Page<InventoryViewDTO> getInventoryViewListWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return inventoryRepository.getInventoryViewListWithPaging(pageable);
    }

    // 페이징네이션 구현 - 상품명 검색 조회
    public Page<InventoryViewDTO> findInventoryViewDTOByProductName(String productName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String searchPattern = "%" + productName + "%";

        Page<InventoryViewDTO> inventoryViewDTOS = inventoryRepository.findInventoryViewDTOByProductName(searchPattern, pageable);
        if (inventoryViewDTOS.isEmpty()) {
            throw new IllegalArgumentException("해당 제품의 재고가 존재하지 않습니다.");

        } else {
            return inventoryViewDTOS;
        }
    }

    // 개별 재고의 로그 조회
    public List<InventoryLog> getInventoryLogByInventoryId(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재고가 존재하지 않습니다."));
        return inventoryLogRepository.findByInventoryOrderByInventoryLogCreatedDesc(inventory);
    }

    @Transactional
    public void updateInventory(Long inventoryId, InventoryDTO inventoryDTO, String reason, String userId) {
        try {
            Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() ->
                    new IllegalArgumentException("재고를 찾을 수 없습니다."));

            // 기존에 등록된 재고와 입력한 재고 차이 계산
            long currentstock = inventory.getAvailableStock();
            long totalstock = inventoryDTO.getAvailableStock();
            long difference = currentstock - totalstock;

            if (difference == 0) {
                return;
            }

            // 재고 테이블 업데이트
            inventory.setInventoryId(inventoryDTO.getInventoryId());
            inventory.setStorageId(inventoryDTO.getStorageId());

            // Product 객체를 찾아서 설정
            Product product = productRepository.findProductByProductId(inventoryDTO.getProductId().intValue());
            inventory.setProduct(product);

            inventory.setLotNumber(createRotNum(product));
            inventory.setLocationCode(inventoryDTO.getLocationCode());
            inventory.setAvailableStock(inventoryDTO.getAvailableStock());
            inventory.setAllocatedStock(totalstock);
            inventory.setDisposedStock(inventoryDTO.getDisposedStock());
            inventory.setInventoryExpiryDate(inventoryDTO.getInventoryExpiryDate());
            // 수정일자는 현재 시간으로 업데이트, 생성일자는 변경하지 않음 (최초 생성 시점으로 유지)
            inventory.setInventoryUpdatedAt(LocalDateTime.now());

            // 재고 로그 생성
            InventoryLog log = new InventoryLog();
            log.setInventory(inventory);
            log.setQuantityChanged(Math.abs(difference));
            log.setTransactionType(InventoryLogTransactionType.ADJUSTMENT);
            log.setUser_id(userId);
            log.setInventoryLogCreated(LocalDateTime.now());

            // 증가, 감소 여부와 사유 작성
            String direction = difference < 0 ? "증가" : "감소";
            log.setInventoryLogContent(direction + " : " + reason);

            inventoryLogRepository.save(log);

        } catch (RuntimeException e) {
            throw new IllegalArgumentException("재고 수정 중 문제가 발생되었습니다.");
        }

    }

    @Transactional
    public void deleteInventory(Long inventoryId) {
        Optional<Inventory> findInventory = inventoryRepository.findById(inventoryId);
        if (findInventory.isPresent()) {
            inventoryRepository.deleteById(inventoryId);
        } else {
            throw new IllegalArgumentException("존재하지 않는 재고 ID 입니다.");
        }

    }

    @Transactional
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        try {
            // 현재 시간 설정
            LocalDateTime now = LocalDateTime.now();

            // ID를 제외하고 엔티티 생성
            Inventory inventory = new Inventory();
            inventory.setStorageId(inventoryDTO.getStorageId());

            // Product 객체를 찾아서 설정
            Product product = productRepository.findProductByProductId(inventoryDTO.getProductId().intValue());
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
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("재고 생성 중 오류가 발생되었습니다.");
        }

    }

    // 로트 번호 생성 메소드
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

    // 로트 번호 생성 시 필요한 일련 번호 생성 메소드
    public int getNextSequenceForProductToday(Integer productId) {
        // 당일 날짜 접두사 생성
        LocalDate today = LocalDate.now();
        String datePrefix = String.format("C%%__%02d%02d%%", today.getYear() % 100, today.getMonthValue());

        // 최대 lot_number 조회
        String maxLotNumber = inventoryRepository.findMaxLotNumberByProductAndDate(productId, datePrefix);

        if (maxLotNumber == null) {
            return 1;
        }

        // lot_number에서 일련번호 추출 (마지막 3자리)
        String sequencePart = maxLotNumber.substring(maxLotNumber.length() - 3);
        return Integer.parseInt(sequencePart) + 1;
    }

    @Override
    public void notifyShipmentCompleted(Inventory inventory, Integer userId, Long difference) {
        // 재고 로그 생성
        InventoryLog log = new InventoryLog();
        log.setInventory(inventory);
        log.setQuantityChanged(Math.abs(difference));
        log.setTransactionType(InventoryLogTransactionType.SHIPMENT);
        log.setUser_id(String.valueOf(userId));
        log.setInventoryLogContent("출고 완료에 따른 재고 등록");
        log.setInventoryLogCreated(LocalDateTime.now());

        inventoryLogRepository.save(log);

    }

    @Override
    @Transactional
    public void notifyStorageCompleted(Integer purchaseId, Integer userId) {

        // 1. 해당 발주 ID에 대한 발주 아이템 리스트 가져오기
        List<PurchaseItem> purchaseItems = purchaseItemRepository.findByPurchasePurchaseId(purchaseId);

        // 2. 각 PurchaseItem에 대해 Inventory 생성
        for (PurchaseItem purchaseItem : purchaseItems) {
            try {
                Product product = productRepository.findProductByProductId(purchaseItem.getProductId());
                if (product == null) {
                    System.err.println("제품 ID가 유효하지 않습니다: " + purchaseItem.getProductId());
                    continue;
                }

                // 랜덤 위치 코드
                Random random = new Random();
                long locationCode = 100 + random.nextInt(900);

                // 유통기한 계산 (현재 날짜 + 상품의 유통기한 일수)
                LocalDate expiryDate = LocalDate.now().plusDays(product.getExpirationDate());

                // 재고 생성
                Inventory inventory = new Inventory();
                inventory.setStorageId(purchaseId.longValue()); // 발주 ID를 입고 ID로 사용
                inventory.setProduct(product);
                inventory.setLotNumber(createRotNum(product));
                inventory.setLocationCode(locationCode);
                inventory.setAvailableStock(purchaseItem.getProductQuantity());
                inventory.setAllocatedStock(0);
                inventory.setDisposedStock(0);
                inventory.setInventoryExpiryDate(expiryDate);
                inventory.setInventoryCreatedAt(LocalDateTime.now());
                inventory.setInventoryUpdatedAt(LocalDateTime.now());

                Inventory savedInventory = inventoryRepository.save(inventory);

                // 3. 재고 로그 생성
                InventoryLog log = new InventoryLog();
                log.setInventory(savedInventory);
                log.setQuantityChanged(Math.abs(purchaseItem.getProductQuantity()));
                log.setTransactionType(InventoryLogTransactionType.STORAGE);
                log.setUser_id(String.valueOf(userId));
                log.setInventoryLogContent("입고 완료에 따른 재고 등록");
                log.setInventoryLogCreated(LocalDateTime.now());

                inventoryLogRepository.save(log);

                System.out.println("재고 등록 완료: 제품 ID = " + purchaseItem.getProductId() +
                        ", 수량 = " + purchaseItem.getProductQuantity());
            } catch (Exception e) {
                System.err.println("재고 등록 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}



