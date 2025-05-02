package com.ohgiraffers.warehousemanagement.wms.shipment.controller;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.Inventory;
import com.ohgiraffers.warehousemanagement.wms.inventory.service.InventoryService;
import com.ohgiraffers.warehousemanagement.wms.product.model.entity.Product;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import com.ohgiraffers.warehousemanagement.wms.sales.model.dto.SalesDTO;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.Sales;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesItem;
import com.ohgiraffers.warehousemanagement.wms.sales.service.SalesService;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.service.ShipmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 출고 관련 HTTP 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);
    private final ShipmentService shipmentService;
    private final SalesService salesService;
    private final InventoryService inventoryService;
    private final ProductService productService;

    public ShipmentController(ShipmentService shipmentService, SalesService salesService, InventoryService inventoryService, ProductService productService) {
        this.shipmentService = shipmentService;
        this.salesService = salesService;
        this.inventoryService = inventoryService;
        this.productService = productService;
        log.info("ShipmentController 초기화 - shipmentService: {}, salesService: {}, inventoryService: {}, productService: {}",
                shipmentService, salesService, inventoryService, productService);
    }

    @GetMapping
    public String showShipmentList(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchTerm,
            Model model) {
        log.info("출고 목록 표시 - 페이지: {}, 크기: {}, 상태: {}, 검색어: {}", page, size, status, searchTerm);
        try {
            Long currentUserId = null;
            try {
                AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
                currentUserId = authDetails.getUserId();
            } catch (NumberFormatException e) {
                log.error("담당자 ID 변환 실패: {}", currentUserId);
                model.addAttribute("error", "담당자 ID가 유효한 숫자가 아닙니다: " + currentUserId);
                return "shipments/shipment";
            }
            model.addAttribute("currentUserId", currentUserId);

            ShipmentPageResponseDTO shipmentPage = shipmentService.getAllShipments(page, size, status, searchTerm);
            model.addAttribute("shipments", shipmentPage.getShipments());
            model.addAttribute("currentPage", shipmentPage.getCurrentPage());
            model.addAttribute("totalPages", shipmentPage.getTotalPages());
            model.addAttribute("size", shipmentPage.getSize());
            model.addAttribute("totalItems", shipmentPage.getTotalItems());
            model.addAttribute("startItem", shipmentPage.getStartItem());
            model.addAttribute("endItem", shipmentPage.getEndItem());

            // 모든 수주 ID 조회
            List<SalesDTO> allSales = salesService.getAllSales();
            List<Integer> saleIds = allSales.stream()
                    .map(SalesDTO::getSalesId)
                    .collect(Collectors.toList());
            if (saleIds.isEmpty()) {
                log.warn("수주 ID 목록 조회 실패, 빈 리스트 반환");
            } else {
                log.info("조회된 수주 ID 목록: {}", saleIds);
            }
            model.addAttribute("saleIds", saleIds);
            return "shipments/shipment";
        } catch (Exception e) {
            log.error("출고 목록 조회 중 오류: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment";
        }
    }

    @GetMapping("/{id}")
    public String showShipmentDetail(@PathVariable("id") Integer id, Model model) {
        log.info("출고 ID: {} 상세 정보 표시", id);
        try {
            ShipmentResponseDTO shipment = shipmentService.getShipmentById(id);
            if (shipment == null) {
                log.warn("출고 ID: {}에 해당하는 데이터 없음", id);
                model.addAttribute("error", "해당 ID의 출고 정보를 찾을 수 없습니다: " + id);
                return "shipments/shipment-detail";
            }
            model.addAttribute("shipment", shipment);

            Integer saleId = shipment.getSaleId();
            Sales sales = salesService.getSalesBySalesId(saleId);
            if (sales == null) {
                log.warn("수주 ID: {}에 해당하는 데이터 없음", saleId);
                model.addAttribute("error", "해당 ID의 수주 데이터를 찾을 수 없습니다: " + saleId);
                return "shipments/shipment-detail";
            }
            List<SalesItem> saleItems = sales.getSalesItems();
            if (saleItems == null || saleItems.isEmpty()) {
                log.warn("수주 ID: {}에 대한 상품 목록 조회 실패", saleId);
                model.addAttribute("saleItems", new ArrayList<>());
            } else {
                List<Map<String, Object>> saleItemsWithDetails = new ArrayList<>();
                for (SalesItem item : saleItems) {
                    Map<String, Object> itemWithDetails = new HashMap<>();
                    itemWithDetails.put("productId", item.getProductId());
                    // 상품명 조회
                    try {
                        Product product = productService.findProductById(item.getProductId());
                        itemWithDetails.put("productName", product.getProductName());
                    } catch (Exception e) {
                        log.warn("상품명 조회 실패 - 상품 ID: {}", item.getProductId());
                        itemWithDetails.put("productName", "-");
                    }
                    itemWithDetails.put("salesItemsQuantity", item.getSalesItemsQuantity());
                    // lotNumber 직접 접근 (getter 없음, 리플렉션 사용)
                    try {
                        Field lotNumberField = SalesItem.class.getDeclaredField("lotNumber");
                        lotNumberField.setAccessible(true);
                        String lotNumber = (String) lotNumberField.get(item);
                        itemWithDetails.put("lotNumber", lotNumber);
                    } catch (Exception e) {
                        log.warn("로트 번호 조회 실패 - 상품 ID: {}", item.getProductId());
                        itemWithDetails.put("lotNumber", "-");
                    }
                    // 재고량 조회
                    try {
                        Inventory inventory = inventoryService.findTopByProductIdOrderByInventoryExpiryDateAsc(item.getProductId());
                        Long availableStock = inventory != null ? inventory.getAvailableStock() : 0L;
                        itemWithDetails.put("availableStock", availableStock.intValue()); // long to int 캐스팅
                    } catch (Exception e) {
                        log.warn("재고 조회 실패 - 상품 ID: {}", item.getProductId());
                        itemWithDetails.put("availableStock", 0);
                    }
                    saleItemsWithDetails.add(itemWithDetails);
                }
                model.addAttribute("saleItems", saleItemsWithDetails);
            }

            log.info("출고 정보 조회 성공: {}", shipment);
            return "shipments/shipment-detail";
        } catch (Exception e) {
            log.error("출고 상세 조회 실패: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment-detail";
        }
    }

    @GetMapping("/sale-items/{saleId}")
    @ResponseBody
    public List<Map<String, Object>> getSaleItems(@PathVariable("saleId") Integer saleId) {
        log.info("수주 ID: {}로 상품 목록 조회", saleId);
        try {
            Sales sales = salesService.getSalesBySalesId(saleId);
            if (sales == null) {
                log.warn("수주 ID: {}에 해당하는 데이터 없음", saleId);
                return new ArrayList<>();
            }
            List<SalesItem> saleItems = sales.getSalesItems();
            if (saleItems == null || saleItems.isEmpty()) {
                log.warn("수주 ID: {}에 대한 상품 목록 조회 실패", saleId);
                return new ArrayList<>();
            }
            List<Map<String, Object>> saleItemsWithDetails = new ArrayList<>();
            for (SalesItem item : saleItems) {
                Map<String, Object> itemWithDetails = new HashMap<>();
                itemWithDetails.put("productId", item.getProductId());
                // 상품명 조회
                try {
                    Product product = productService.findProductById(item.getProductId());
                    itemWithDetails.put("productName", product.getProductName());
                } catch (Exception e) {
                    log.warn("상품명 조회 실패 - 상품 ID: {}", item.getProductId());
                    itemWithDetails.put("productName", "-");
                }
                itemWithDetails.put("salesItemsQuantity", item.getSalesItemsQuantity());
                // lotNumber 직접 접근 (getter 없음, 리플렉션 사용)
                try {
                    Field lotNumberField = SalesItem.class.getDeclaredField("lotNumber");
                    lotNumberField.setAccessible(true);
                    String lotNumber = (String) lotNumberField.get(item);
                    itemWithDetails.put("lotNumber", lotNumber);
                } catch (Exception e) {
                    log.warn("로트 번호 조회 실패 - 상품 ID: {}", item.getProductId());
                    itemWithDetails.put("lotNumber", "-");
                }
                // 재고량 조회
                try {
                    Inventory inventory = inventoryService.findTopByProductIdOrderByInventoryExpiryDateAsc(item.getProductId());
                    Long availableStock = inventory != null ? inventory.getAvailableStock() : 0L;
                    itemWithDetails.put("availableStock", availableStock.intValue()); // long to int 캐스팅
                } catch (Exception e) {
                    log.warn("재고 조회 실패 - 상품 ID: {}", item.getProductId());
                    itemWithDetails.put("availableStock", 0);
                }
                saleItemsWithDetails.add(itemWithDetails);
            }
            return saleItemsWithDetails;
        } catch (Exception e) {
            log.error("수주 상품 목록 조회 실패 - 수주 ID: {}: {}", saleId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @PostMapping
    public String saveNewShipment(
            @Valid @ModelAttribute("shipment") ShipmentCreateDTO shipmentCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        log.info("새 출고 저장: {}", shipmentCreateDTO);
        log.info("새 출고 저장 - 수주 ID: {}", shipmentCreateDTO.getSaleId());
        if (bindingResult.hasErrors()) {
            log.warn("유효성 검사 오류: {}", bindingResult.getAllErrors());
            model.addAttribute("error", "입력 데이터가 유효하지 않습니다: " + bindingResult.getAllErrors());
            return "shipments/shipment";
        }
        try {
            // 수주 ID가 null인지 확인
            if (shipmentCreateDTO.getSaleId() == null) {
                log.error("수주 ID가 null입니다.");
                model.addAttribute("error", "수주 ID가 누락되었습니다.");
                return "shipments/shipment";
            }
            // 담당자 ID가 null인지 확인
            if (shipmentCreateDTO.getUserId() == null) {
                log.error("담당자 ID가 null입니다.");
                model.addAttribute("error", "담당자 ID가 누락되었습니다.");
                return "shipments/shipment";
            }
            ShipmentResponseDTO createdShipment = shipmentService.createShipment(shipmentCreateDTO);
            redirectAttributes.addFlashAttribute("message", "출고 #" + createdShipment.getShipmentId() + "가 생성되었습니다.");
            return "redirect:/shipments";
        } catch (Exception e) {
            log.error("출고 저장 실패: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 저장 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment";
        }
    }

    @PostMapping("/update/{id}")
    public String updateExistingShipment(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("shipment") ShipmentCreateDTO updateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        log.info("출고 ID: {} 업데이트 요청", id);
        log.debug("업데이트 DTO: {}", updateDTO);
        if (bindingResult.hasErrors()) {
            log.warn("유효성 검사 오류: {}", bindingResult.getAllErrors());
            model.addAttribute("error", "입력 데이터가 유효하지 않습니다: " + bindingResult.getAllErrors());
            return "shipments/shipment";
        }
        try {
            // 수주 ID가 null인지 확인
            if (updateDTO.getSaleId() == null) {
                log.error("수주 ID가 null입니다.");
                model.addAttribute("error", "수주 ID가 누락되었습니다.");
                return "shipments/shipment";
            }
            // 담당자 ID가 null인지 확인
            if (updateDTO.getUserId() == null) {
                log.error("담당자 ID가 null입니다.");
                model.addAttribute("error", "담당자 ID가 누락되었습니다.");
                return "shipments/shipment";
            }
            ShipmentResponseDTO updatedShipment = shipmentService.updateShipment(id, updateDTO);
            redirectAttributes.addFlashAttribute("message", "출고 #" + updatedShipment.getShipmentId() + "가 수정되었습니다.");
            return "redirect:/shipments";
        } catch (Exception e) {
            log.error("출고 업데이트 실패: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment";
        }
    }
}