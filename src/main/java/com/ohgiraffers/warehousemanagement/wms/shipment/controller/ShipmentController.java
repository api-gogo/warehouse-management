package com.ohgiraffers.warehousemanagement.wms.shipment.controller;

import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.model.dto.ShipmentResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.shipment.service.ShipmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 출고(Shipment) 관련 요청을 처리하는 컨트롤러
 * - 출고 목록 조회, 상세 정보 조회, 생성, 수정 기능 제공
 * - "/shipments" 경로로 들어오는 모든 요청 처리
 */
@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    // 로깅을 위한 Logger 객체 초기화
    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);

    // 출고 서비스 의존성 주입
    private final ShipmentService shipmentService;

    /**
     * 생성자를 통한 의존성 주입
     * @param shipmentService 출고 관련 비즈니스 로직을 처리하는 서비스
     */
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
        log.info("ShipmentController initialized with shipmentService: {}", shipmentService);
    }

    /**
     * 출고 목록 페이지 조회
     * - 페이징, 상태 필터링, 검색 기능 제공
     * 
     * @param page 요청 페이지 번호 (기본값: 0)
     * @param size 페이지당 항목 수 (기본값: 10)
     * @param status 출고 상태 필터 (선택 사항)
     * @param searchTerm 검색어 (선택 사항)
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 출고 목록 페이지 뷰 이름
     */
    @GetMapping
    public String showShipmentList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchTerm,
            Model model) {
        log.info("Showing shipment list - Page: {}, Size: {}, Status: {}, SearchTerm: {}", page, size, status, searchTerm);
        try {
            // 서비스를 통해 출고 목록 데이터 조회
            ShipmentPageResponseDTO shipmentPage = shipmentService.getAllShipments(page, size, status, searchTerm);
            shipmentPage.getShipments().forEach(shipment ->
                    log.info("Shipment ID: {}, Status: {}", shipment.getShipmentId(), shipment.getShipmentStatus()));
            
            // 조회된 데이터를 모델에 추가
            model.addAttribute("shipments", shipmentPage.getShipments());
            model.addAttribute("currentPage", shipmentPage.getCurrentPage());
            model.addAttribute("totalPages", shipmentPage.getTotalPages());
            model.addAttribute("size", shipmentPage.getSize());
            model.addAttribute("totalItems", shipmentPage.getTotalItems());
            model.addAttribute("startItem", shipmentPage.getStartItem());
            model.addAttribute("endItem", shipmentPage.getEndItem());
            
            log.info("Rendering template: shipments/shipment");
            return "shipments/shipment";
        } catch (Exception e) {
            // 오류 발생 시 에러 메시지를 모델에 추가하고 동일한 페이지 반환
            log.error("Error while fetching shipment list: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment";
        }
    }

    /**
     * 출고 상세 정보 조회
     * 
     * @param id 조회할 출고 ID
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 출고 상세 정보 페이지 뷰 이름
     */
    @GetMapping("/{id}")
    public String showShipmentDetail(@PathVariable("id") Integer id, Model model) {
        log.info("Displaying shipment detail for ID: {}", id);
        try {
            // 서비스를 통해 특정 ID의 출고 정보 조회
            ShipmentResponseDTO shipment = shipmentService.getShipmentById(id);
            if (shipment == null) {
                // 출고 정보가 없는 경우 에러 메시지 추가
                log.warn("No shipment found for ID: {}", id);
                model.addAttribute("error", "해당 ID의 출고 정보를 찾을 수 없습니다: " + id);
            } else {
                // 조회된 출고 정보를 모델에 추가
                model.addAttribute("shipment", shipment);
                log.info("Shipment found: {}", shipment);
            }
            return "shipments/shipment-detail";
        } catch (Exception e) {
            // 오류 발생 시 에러 메시지를 모델에 추가
            log.error("Failed to fetch shipment for detail: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment-detail";
        }
    }

    /**
     * 새 출고 정보 저장
     * 
     * @param shipmentCreateDTO 생성할 출고 정보 DTO
     * @param bindingResult 입력값 유효성 검증 결과
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 성공 시 출고 목록 페이지로 리다이렉트, 실패 시 출고 목록 페이지
     */
    @PostMapping
    public String saveNewShipment(
            @Valid @ModelAttribute("shipment") ShipmentCreateDTO shipmentCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        log.info("Saving new shipment: {}", shipmentCreateDTO);
        
        // 유효성 검증 실패 시 오류 메시지 추가하고 폼 페이지 반환
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("error", "입력 데이터가 유효하지 않습니다.");
            return "shipments/shipment";
        }
        
        try {
            // 서비스를 통해 새 출고 정보 생성
            ShipmentResponseDTO createdShipment = shipmentService.createShipment(shipmentCreateDTO);
            // 성공 메시지를 플래시 속성에 추가하고 목록 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("message", "출고 #" + createdShipment.getShipmentId() + "가 생성되었습니다.");
            return "redirect:/shipments";
        } catch (Exception e) {
            // 오류 발생 시 에러 메시지를 모델에 추가
            log.error("Failed to save shipment: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "shipments/shipment";
        }
    }

    /**
     * 기존 출고 정보 수정
     * 
     * @param id 수정할 출고 ID
     * @param updateDTO 수정 정보가 담긴 DTO
     * @param bindingResult 입력값 유효성 검증 결과
     * @param redirectAttributes 리다이렉트 시 전달할 속성
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 성공 시 출고 목록 페이지로 리다이렉트, 실패 시 출고 목록 페이지
     */
    @PostMapping("/update/{id}")
    public String updateExistingShipment(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("shipment") ShipmentCreateDTO updateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        log.info("Received update request for shipment ID: {}", id);
        log.debug("Update DTO: {}", updateDTO);
        
        // 유효성 검증 실패 시 오류 메시지 추가하고 폼 페이지 반환
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("error", "입력 데이터가 유효하지 않습니다.");
            return "shipments/shipment";
        }
        
        try {
            // 서비스를 통해 출고 정보 수정
            ShipmentResponseDTO updatedShipment = shipmentService.updateShipment(id, updateDTO);
            // 성공 메시지를 플래시 속성에 추가하고 목록 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("message", "출고 #" + updatedShipment.getShipmentId() + "가 수정되었습니다.");
            return "redirect:/shipments";
        } catch (Exception e) {
            // 오류 발생 시 에러 메시지를 모델에 추가
            log.error("Failed to update shipment: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "shipments/shipment";
        }
    }
}