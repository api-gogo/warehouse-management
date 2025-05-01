package com.ohgiraffers.warehousemanagement.wms.purchases.controller;


import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseItemDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.service.PurchasesItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Validated
public class PurchaseItemController {
    private PurchasesItemService purchasesItemService;

    @Autowired
    public PurchaseItemController(PurchasesItemService purchasesItemService) {
        this.purchasesItemService = purchasesItemService;
    }

    //단일 조회
    @GetMapping("/purchase-detail/{id}")
    public String getPurchaseItemById(@PathVariable Integer id, Model model) {
        PurchaseItemDTO purchaseItemDTO = purchasesItemService.getPurchaseItemById(id);
        model.addAttribute("purchaseItem", purchaseItemDTO);
        model.addAttribute("pageTitle", "발주 항목 상세 정보");
        model.addAttribute("cardTitle", "발주 항목 상세");
        model.addAttribute("cardDescription", "발주 항목의 상세 정보를 확인할 수 있습니다.");

        return "purchases/purchase-item-detail"; // 새로운 상세 페이지 템플릿
    }

    // 생성
    @PostMapping
    public String createPurchaseItem(@Validated @ModelAttribute PurchaseItemDTO purchaseItemDTO, RedirectAttributes rdtat) {
        PurchaseItemDTO savedDTO = purchasesItemService.createPurchaseItem(purchaseItemDTO);
        if (savedDTO != null) {
            rdtat.addFlashAttribute("message", "발주 항목이 등록되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 항목 등록에 실패하였습니다. 다시 시도해주세요.");
        }
        return "redirect:/purchases"; // 생성 후 발주 목록으로 리다이렉트
    }
}
