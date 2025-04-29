package com.ohgiraffers.warehousemanagement.wms.inventory.controller;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // 재고 전체 조회
    @GetMapping
    public String getInventoryList(@RequestParam(name = "searchProductId", required = false) Long productId,                                   Model model) {

        List<InventoryDTO> inventories = null;

        try {
            // 필터링 없는 전체 조회일 경우
            if (productId == null) {
                inventories = inventoryService.findAllInventories();
            } else {
                inventories = inventoryService.findAllInventoriesByProductId(productId);
            }

            model.addAttribute("inventories", inventories);
            model.addAttribute("activeMenu", "inventory");
            model.addAttribute("today", LocalDate.now());

            // 로그 추가
            System.out.println("인벤토리 목록 조회 - 데이터 개수: " + inventories.size());
            return "/inventory/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/inventory/list";
        }
    }


    // 재고 상세 정보 확인
    @GetMapping("/detail/{inventoryId}")
    public ModelAndView inventoryDetail(@PathVariable("inventoryId") Long inventoryId, ModelAndView mv) {
        InventoryDTO inventory = inventoryService.findInventoryById(inventoryId);
        mv.addObject("inventory", inventory);
        mv.addObject("activeMenu", "inventory");
        mv.setViewName("/inventory/detail");
        return mv;
    }

    // 재고 수정 페이지
    @GetMapping("/edit/{inventoryId}")
    public ModelAndView editInventory(@PathVariable("inventoryId") Long inventoryId, ModelAndView mv) {
        InventoryDTO inventory = inventoryService.findInventoryById(inventoryId);
        mv.addObject("inventory", inventory);
        mv.addObject("activeMenu", "inventory");
        mv.setViewName("/inventory/edit");
        return mv;
    }
    
    // 재고 수정 처리
    @PostMapping("/edit/{inventoryId}")
    public String updateInventory(@PathVariable("inventoryId") Long inventoryId, @ModelAttribute InventoryDTO inventoryDTO) {
        inventoryService.updateInventory(inventoryId, inventoryDTO);
        return "redirect:/inventories/detail/{inventoryId}";
    }

    // 재고 삭제
    @GetMapping("/delete/{inventoryId}")
    public String deleteInventory(@PathVariable("inventoryId") Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return "redirect:/inventories";
    }

    // 재고 등록 페이지
    @GetMapping("/add")
    public String inventoryAddForm(Model model) {
        model.addAttribute("activeMenu", "inventory");
        return "/inventory/add";
    }

    // 재고 등록 처리
    @PostMapping("/add")
    public String inventoryAdd(@ModelAttribute InventoryDTO inventoryDTO) {
        inventoryService.createInventory(inventoryDTO);
        return "redirect:/inventories";
    }
}