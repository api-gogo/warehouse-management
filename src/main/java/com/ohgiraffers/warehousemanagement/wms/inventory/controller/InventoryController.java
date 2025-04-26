package com.ohgiraffers.warehousemanagement.wms.inventory.controller;

import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    // 재고 전체 조회
    @GetMapping("/list")
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
        return "inventory/list";
    }

    // 재고 등록 페이지
    @GetMapping("inventoryAdd")
    public String inventoryAdd() {
        return "add";
    }


    // 재고 등록
    /*@PostMapping("inventoryAdd")
    public ModelAndView inventoryAdd(InventoryDTO inventoryDTO) {

    }*/




}
