package com.ohgiraffers.warehousemanagement.wms.inventory.controller;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public String getinventoryList(@RequestParam(name = "searchProductId", required = false) String productId, Model model) {
        List<InventoryDTO> inventories;
        // searchProductId 가 있으면 해당 상품 번호인 재고 조회
        if (productId != null && !productId.isEmpty()) {
            inventories = inventoryService.findAllInventoriesByProductId(productId);
        } else {
            // searchProductId 가 없으면 전체 조회
            inventories = inventoryService.findAllInventories();
        }
        model.addAttribute("inventories", inventories);
        model.addAttribute("activeMenu", "inventory");
        
        // 로그 추가
        System.out.println("인벤토리 목록 조회 - 데이터 개수: " + inventories.size());
        
        return "/inventory/list";
    }


    // 재고 상세 조회
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
        return "redirect:/inventories/list";
    }
}