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

@Controller
@RequestMapping("/shipments")
public class ShipmentController {

    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
        log.info("ShipmentController initialized with shipmentService: {}", shipmentService);
    }

    @GetMapping
    public String showShipmentList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String searchTerm,
            Model model) {
        log.info("Showing shipment list - Page: {}, Size: {}, Status: {}, SearchTerm: {}", page, size, status, searchTerm);
        try {
            ShipmentPageResponseDTO shipmentPage = shipmentService.getAllShipments(page, size, status, searchTerm);
            shipmentPage.getShipments().forEach(shipment ->
                    log.info("Shipment ID: {}, Status: {}", shipment.getShipmentId(), shipment.getShipmentStatus()));
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
            log.error("Error while fetching shipment list: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment";
        }
    }

    @GetMapping("/{id}")
    public String showShipmentDetail(@PathVariable("id") Integer id, Model model) {
        log.info("Displaying shipment detail for ID: {}", id);
        try {
            ShipmentResponseDTO shipment = shipmentService.getShipmentById(id);
            if (shipment == null) {
                log.warn("No shipment found for ID: {}", id);
                model.addAttribute("error", "해당 ID의 출고 정보를 찾을 수 없습니다: " + id);
            } else {
                model.addAttribute("shipment", shipment);
                log.info("Shipment found: {}", shipment);
            }
            return "shipments/shipment-detail";
        } catch (Exception e) {
            log.error("Failed to fetch shipment for detail: {}", e.getMessage(), e);
            model.addAttribute("error", "출고 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "shipments/shipment-detail";
        }
    }

    @PostMapping
    public String saveNewShipment(
            @Valid @ModelAttribute("shipment") ShipmentCreateDTO shipmentCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        log.info("Saving new shipment: {}", shipmentCreateDTO);
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("error", "입력 데이터가 유효하지 않습니다.");
            return "shipments/shipment";
        }
        try {
            ShipmentResponseDTO createdShipment = shipmentService.createShipment(shipmentCreateDTO);
            redirectAttributes.addFlashAttribute("message", "출고 #" + createdShipment.getShipmentId() + "가 생성되었습니다.");
            return "redirect:/shipments";
        } catch (Exception e) {
            log.error("Failed to save shipment: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
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
        log.info("Received update request for shipment ID: {}", id);
        log.debug("Update DTO: {}", updateDTO);
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("error", "입력 데이터가 유효하지 않습니다.");
            return "shipments/shipment";
        }
        try {
            ShipmentResponseDTO updatedShipment = shipmentService.updateShipment(id, updateDTO);
            redirectAttributes.addFlashAttribute("message", "출고 #" + updatedShipment.getShipmentId() + "가 수정되었습니다.");
            return "redirect:/shipments";
        } catch (Exception e) {
            log.error("Failed to update shipment: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "shipments/shipment";
        }
    }
}