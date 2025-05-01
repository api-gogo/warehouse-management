package com.ohgiraffers.warehousemanagement.wms.purchases.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.dto.PurchaseItemDTO;
import com.ohgiraffers.warehousemanagement.wms.purchases.model.entity.PurchaseStatus;
import com.ohgiraffers.warehousemanagement.wms.purchases.service.PurchaseService;
import com.ohgiraffers.warehousemanagement.wms.purchases.service.PurchasesItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/purchases")
@Validated
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final ObjectMapper objectMapper;
    private final PurchasesItemService purchasesItemService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, ObjectMapper objectMapper, PurchasesItemService purchasesItemService) {
        this.purchaseService = purchaseService;
        this.objectMapper = objectMapper;
        this.purchasesItemService = purchasesItemService;
    }

    // Helper method to convert status string to PurchaseStatus enum
    private PurchaseStatus getStatusFromString(String statusStr) {
        if (statusStr == null || statusStr.isEmpty()) {
            throw new IllegalArgumentException("상태가 지정되지 않았습니다.");
        }
        for (PurchaseStatus status : PurchaseStatus.values()) {
            if (status.getLabel().equals(statusStr)) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 상태: " + statusStr);
    }

    // 메인 페이지
    @GetMapping({"", "/"})
    public String mainPage(Model model) {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        model.addAttribute("pageTitle", "발주 관리");
        model.addAttribute("cardTitle", "발주 목록");
        model.addAttribute("cardDescription", "모든 발주 내역을 확인하고 관리할 수 있습니다.");
        return "purchases/list";
    }

    // 전체조회
    @GetMapping("/list")
    public String getAllPurchases(Model model) {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        model.addAttribute("pageTitle", "발주 관리");
        model.addAttribute("cardTitle", "발주 목록");
        model.addAttribute("cardDescription", "모든 발주 내역을 확인하고 관리할 수 있습니다.");
        return "purchases/list";
    }

    // status를 이용하여 조회
    @GetMapping("/search/status")
    public String getAllPurchasesByStatus(Model model, @RequestParam String status) {
        PurchaseStatus purchaseStatus = getStatusFromString(status);
        List<PurchaseDTO> purchases = purchaseService.searchByStatus(purchaseStatus);
        model.addAttribute("purchases", purchases);
        model.addAttribute("selectedStatus", status); // Use string label for form persistence
        model.addAttribute("pageTitle", "발주 관리 - " + status + " 조회");
        model.addAttribute("cardTitle", status + " 상태 발주 목록");
        model.addAttribute("cardDescription", status + " 상태의 모든 발주 내역을 확인하고 관리할 수 있습니다.");
        return "purchases/list";
    }

    // 담당자 id를 이용하여 조회
    @GetMapping("/search/user")
    public String getPurchasesByUser(Model model, @RequestParam Integer userId) {
        List<PurchaseDTO> purchases = purchaseService.searchByUserId(userId);
        model.addAttribute("purchases", purchases);
        model.addAttribute("selectedUser", userId);
        model.addAttribute("pageTitle", "발주 관리 - 담당자별 조회");
        model.addAttribute("cardTitle", "담당자별 발주 목록");
        model.addAttribute("cardDescription", "선택한 담당자의 발주 내역을 확인하고 관리할 수 있습니다.");
        return "purchases/list";
    }

    // 기간을 이용하여 조회
    @GetMapping("/search/date")
    public String getPurchasesByDateRange(
            Model model,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<PurchaseDTO> purchases = purchaseService.searchByPurchaseDateRange(startDate, endDate);
        model.addAttribute("purchases", purchases);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("pageTitle", "발주 관리 - 기간별 조회");
        model.addAttribute("cardTitle", "기간별 발주 목록");
        model.addAttribute("cardDescription", startDate + " ~ " + endDate + " 기간의 발주 내역을 확인하고 관리할 수 있습니다.");
        return "purchases/list";
    }

    // 단일조회
    @GetMapping("/{id}")
    public String getPurchaseById(@PathVariable Integer id, Model model) {
        PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);
        List<PurchaseItemDTO> purchaseItems = purchaseService.getPurchaseItemsByPurchaseId(id);
        int totalQuantity = purchaseItems.stream().mapToInt(PurchaseItemDTO::getProductQuantity).sum();
        long totalAmount = purchaseItems.stream()
                .mapToLong(item -> (long) (item.getPricePerBox() != null ? item.getPricePerBox() : 0) * item.getProductQuantity())
                .sum();
        model.addAttribute("purchase", purchaseDTO);
        model.addAttribute("purchaseItems", purchaseItems);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("pageTitle", "발주 상세 정보");
        return "purchases/purchase-detail";
    }

    // 발주 생성
    @GetMapping("/create")
    public String createPurchase(Model model, @AuthenticationPrincipal AuthDetails authDetails) {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        if (authDetails != null) {
            purchaseDTO.setUserId(authDetails.getUserId());
        }
        model.addAttribute("purchaseDTO", purchaseDTO);
        model.addAttribute("pageTitle", "발주 생성");
        model.addAttribute("cardTitle", "신규 발주 등록");
        model.addAttribute("cardDescription", "새로운 발주를 등록할 수 있습니다.");
        return "purchases/create";
    }

    @PostMapping("/create")
    @Transactional
    public String createPurchase(@Validated @ModelAttribute PurchaseDTO purchaseDTO,
                                 @RequestParam("purchaseItemsData") String purchaseItemsData,
                                 RedirectAttributes rdtat, @AuthenticationPrincipal AuthDetails authDetails) {
        try {
            if (authDetails != null) {
                purchaseDTO.setUserId(authDetails.getUserId());
            } else {
                rdtat.addFlashAttribute("message", "로그인 정보를 찾을 수 없습니다.");
                return "redirect:/purchases/create";
            }
            List<PurchaseItemDTO> purchaseItems = objectMapper.readValue(purchaseItemsData, new TypeReference<List<PurchaseItemDTO>>() {});
            if (purchaseItems.isEmpty()) {
                rdtat.addFlashAttribute("message", "최소 하나 이상의 상품을 추가해야 합니다.");
                return "redirect:/purchases/create";
            }
            PurchaseDTO savedPurchase = purchaseService.createPurchase(purchaseDTO);
            for (PurchaseItemDTO item : purchaseItems) {
                item.setPurchaseId(savedPurchase.getPurchaseId());
                purchasesItemService.createPurchaseItem(item);
            }
            rdtat.addFlashAttribute("message", "발주가 등록되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            rdtat.addFlashAttribute("message", "발주 등록에 실패하였습니다: " + e.getMessage());
            return "redirect:/purchases/create";
        }
        return "redirect:/purchases";
    }

    // 발주 수정
    @GetMapping("/purchase-detail/{id}")
    public String updatePurchase(@PathVariable Integer id, Model model) {
        PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);
        List<PurchaseItemDTO> purchaseItems = purchaseService.getPurchaseItemsByPurchaseId(id);
        model.addAttribute("purchaseDTO", purchaseDTO);
        model.addAttribute("purchaseItems", purchaseItems);
        model.addAttribute("pageTitle", "발주 수정");
        model.addAttribute("cardTitle", "발주 정보 수정");
        model.addAttribute("cardDescription", "발주 정보를 수정할 수 있습니다.");
        return "purchases/purchase-detail";
    }

    @PostMapping("/purchase-detail/{id}")
    public String updatePurchase(Model model, @PathVariable Integer id, @Validated @ModelAttribute PurchaseDTO purchaseDTO,
                                 RedirectAttributes rdtat) {
        PurchaseDTO purchase = purchaseService.updatePurchase(purchaseDTO, id);
        if (purchase != null) {
            rdtat.addFlashAttribute("message", "발주가 수정되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 수정에 실패했습니다.");
        }
        return "redirect:/purchases";
    }

    @PostMapping("/update/{id}")
    public String updatePurchaseProcess(@PathVariable Integer id,
                                        @Validated @ModelAttribute PurchaseDTO purchaseDTO,
                                        RedirectAttributes rdtat) {
        PurchaseDTO purchase = purchaseService.updatePurchase(purchaseDTO, id);
        if (purchase != null) {
            rdtat.addFlashAttribute("message", "발주가 수정되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 수정에 실패했습니다.");
        }
        return "redirect:/purchases/" + id;
    }

    @PostMapping("/api/items/update")
    @ResponseBody
    public ResponseEntity<?> updatePurchaseItems(@RequestBody Map<String, Object> requestData) {
        try {
            Integer purchaseId = (Integer) requestData.get("purchaseId");
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) requestData.get("items");
            if (purchaseId == null || itemsData == null) {
                return ResponseEntity.badRequest().body("구매 ID 또는 상품 데이터가 없습니다.");
            }
            purchasesItemService.deleteAllByPurchaseId(purchaseId);
            List<PurchaseItemDTO> savedItems = new ArrayList<>();
            for (Map<String, Object> item : itemsData) {
                PurchaseItemDTO itemDTO = new PurchaseItemDTO();
                itemDTO.setPurchaseId(purchaseId);
                itemDTO.setProductId(((Number) item.get("productId")).intValue());
                itemDTO.setProductName((String) item.get("productName"));
                itemDTO.setPricePerBox(((Number) item.get("price")).intValue());
                itemDTO.setProductQuantity(((Number) item.get("quantity")).intValue());
                PurchaseItemDTO savedItem = purchasesItemService.createPurchaseItem(itemDTO);
                savedItems.add(savedItem);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "상품 목록이 성공적으로 저장되었습니다.");
            response.put("items", savedItems);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 목록 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 발주 삭제
    @PostMapping("/delete/{id}")
    public String deletePurchase(Model model, @PathVariable Integer id, RedirectAttributes rdtat, Authentication authentication) {
        try {
            purchaseService.deletedpurchase(id);
            rdtat.addFlashAttribute("message", "발주가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            rdtat.addFlashAttribute("message", "삭제할 발주를 찾을 수 없습니다.");
        }
        return "redirect:/purchases";
    }

    // 발주 완료
    @PostMapping("/complete/{id}")
    public String completePurchase(@PathVariable Integer id, RedirectAttributes rdtat, @ModelAttribute PurchaseDTO purchaseDTO, Authentication authentication) {
        boolean completedPurchase = purchaseService.completePurchase(id);
        if (completedPurchase) {
            rdtat.addFlashAttribute("message", "발주가 완료되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 완료가 실패했습니다.");
        }
        return "redirect:/purchases";
    }

    // 발주 거절
    @PostMapping("/reject/{id}")
    public String rejectPurchase(@PathVariable Integer id, RedirectAttributes rdtat, @ModelAttribute PurchaseDTO purchaseDTO) {
        boolean rejectPurchase = purchaseService.rejectPurchase(id);
        if (rejectPurchase) {
            rdtat.addFlashAttribute("message", "발주가 거절되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 거절이 실패했습니다.");
        }
        return "redirect:/purchases";
    }

    // 발주 취소
    @PostMapping("/cancel/{id}")
    public String cancelPurchasePost(@PathVariable Integer id, RedirectAttributes rdtat, @ModelAttribute PurchaseDTO purchaseDTO) {
        boolean cancelPurchase = purchaseService.cancelPurchase(id);
        if (cancelPurchase) {
            rdtat.addFlashAttribute("message", "발주가 취소되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 취소가 실패했습니다.");
        }
        return "redirect:/purchases";
    }

    @GetMapping("/cancel/{id}")
    public String cancelPurchaseGet(@PathVariable Integer id, RedirectAttributes rdtat) {
        boolean cancelPurchase = purchaseService.cancelPurchase(id);
        if (cancelPurchase) {
            rdtat.addFlashAttribute("message", "발주가 취소되었습니다.");
        } else {
            rdtat.addFlashAttribute("message", "발주 취소가 실패했습니다.");
        }
        return "redirect:/purchases";
    }

    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseEntity<?> getProductInfo(@PathVariable Integer productId) {
        try {
            PurchaseItemDTO product = purchaseService.getProductInfo(productId);
            if (product == null) {
                System.out.println("상품 정보를 찾을 수 없습니다. ID: " + productId);
                return ResponseEntity.notFound().build();
            }
            System.out.println("조회된 상품 정보: " + product.toString());
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            System.err.println("상품 정보 조회 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/api/Supplier/{supplierId}")
    @ResponseBody
    public ResponseEntity<?> getSupplierId(@PathVariable Integer supplierId) {
        try {
            Integer verifiedSupplierId = purchaseService.getSupplierid(supplierId);
            return ResponseEntity.ok(verifiedSupplierId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 거래처를 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("거래처 정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}