package com.ohgiraffers.warehousemanagement.wms.inventory.controller;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.entity.InventoryLog;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.repository.InventoryRepository;
import com.ohgiraffers.warehousemanagement.wms.inventory.service.InventoryServicelmpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryServicelmpl inventoryServicelmpl;

    @Autowired
    public InventoryController(InventoryServicelmpl inventoryServicelmpl) {
        this.inventoryServicelmpl = inventoryServicelmpl;
    }

    // 재고 전체 조회
    @GetMapping
    public String getInventoryList(@RequestParam(name = "productName", required = false) String productName,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size, Model model) {
        try {
            Page<InventoryViewDTO> inventoriesPage;

            if (productName != null && !productName.isEmpty()) {
                inventoriesPage = inventoryServicelmpl.findInventoryViewDTOByProductName(productName, page, size);
            } else {
                inventoriesPage = inventoryServicelmpl.getInventoryViewListWithPaging(page, size);
            }

            model.addAttribute("inventories", inventoriesPage.getContent());
            model.addAttribute("currentPage", inventoriesPage.getNumber());
            model.addAttribute("totalPages", inventoriesPage.getTotalPages());
            model.addAttribute("productName", productName);
            model.addAttribute("size", size);
            model.addAttribute("activeMenu", "inventory");
            model.addAttribute("today", LocalDate.now());

            return "/inventory/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/inventory/list";
        }
    }


    // 상품에 해당하는 재고 확인
    @GetMapping("/detail/{productId}")
    public ModelAndView inventoryDetailByProductId(@PathVariable("productId") long productId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "8") int size, ModelAndView mv) {

        Page<InventoryDTO> inventoriesPage = inventoryServicelmpl.findByProductProductIdOrderByInventoryExpiryDateAsc(productId, page, size);


        mv.addObject("inventory", inventoriesPage.getContent());
        mv.addObject("currentPage", inventoriesPage.getNumber());
        mv.addObject("totalPages", inventoriesPage.getTotalPages());
        mv.addObject("totalItems", inventoriesPage.getTotalElements());
        mv.addObject("size", size);
        mv.addObject("activeMenu", "inventory");
        mv.setViewName("/inventory/detail");
        return mv;

    }

    // 재고 상세 정보 조회
    @GetMapping("/{inventoryId}/detail")
    public ModelAndView inventoryDetail(@PathVariable("inventoryId") Long inventoryId, ModelAndView mv) {
        InventoryDTO inventory = inventoryServicelmpl.findInventoryById(inventoryId);
        List<InventoryLog> log = inventoryServicelmpl.getInventoryLogByInventoryId(inventoryId);
        mv.addObject("inventory", inventory);
        mv.addObject("logs", log);
        mv.addObject("activeMenu", "inventory");
        mv.setViewName("/inventory/inventory-detail");
        return mv;
    }


    // 재고 수정 페이지
    @GetMapping("/edit/{inventoryId}")
    public ModelAndView editInventory(@PathVariable("inventoryId") Long inventoryId, ModelAndView mv) {
        InventoryDTO inventory = inventoryServicelmpl.findInventoryById(inventoryId);
        mv.addObject("inventory", inventory);
        mv.addObject("activeMenu", "inventory");
        mv.setViewName("/inventory/edit");
        return mv;
    }
    
    // 재고 수정 처리
    @PostMapping("/edit/{inventoryId}")
    public String updateInventory(@PathVariable("inventoryId") Long inventoryId,
                                  @ModelAttribute InventoryDTO inventoryDTO,
                                  @RequestParam String reason,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        AuthDetails authDetails = (AuthDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = authDetails.getUsername().toString();


        inventoryServicelmpl.updateInventory(inventoryId, inventoryDTO, reason, userId);
        redirectAttributes.addFlashAttribute("inventories", inventoryDTO);
        return "redirect:/inventories/{inventoryId}/detail";
    }

    // 재고 삭제
    @GetMapping("/delete/{inventoryId}")
    public String deleteInventory(@PathVariable("inventoryId") Long inventoryId) {
        inventoryServicelmpl.deleteInventory(inventoryId);
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
        inventoryServicelmpl.createInventory(inventoryDTO);
        return "redirect:/inventories";
    }
}