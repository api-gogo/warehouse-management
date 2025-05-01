package com.ohgiraffers.warehousemanagement.wms.storage.controller;

import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.request.StorageRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response.StorageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response.PurchaseInfoResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import com.ohgiraffers.warehousemanagement.wms.storage.service.StorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/storages")
public class StorageController {

    private static final Logger log = LoggerFactory.getLogger(StorageController.class);
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    //  목록 + 검색
    @GetMapping
    public String getAllStorage(@RequestParam(required = false) String searchKeyword, Model model) {
        List<StorageResponseDTO> storages = (searchKeyword != null && !searchKeyword.isBlank())
                ? storageService.searchStoragesByPurchaseId(searchKeyword)
                : storageService.getAllStorage();
        model.addAttribute("storages", storages);
        model.addAttribute("searchKeyword", searchKeyword);
        return "storages/list";
    }

    //  입고 등록 폼
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("storage", new StorageRequestDTO());
        model.addAttribute("statusList", StorageStatus.values());
        return "storages/create";
    }

    //  입고 등록 처리
    @PostMapping
    public String createStorage(@ModelAttribute("storage") @Valid StorageRequestDTO storageRequestDTO, Model model) {
        log.info("입고 등록 요청: {}", storageRequestDTO);
        try {
            StorageResponseDTO created = storageService.createStorage(storageRequestDTO);
            log.info("입고 등록 성공: ID = {}", created.getStorageId());
            return "redirect:/storages";
        } catch (IllegalArgumentException e) {
            log.warn("입고 등록 실패: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("storage", storageRequestDTO);
            model.addAttribute("statusList", StorageStatus.values());
            return "storages/create";
        }
    }

    //  입고 상세 조회
    @GetMapping("/{id}")
    public String getStorageById(@PathVariable int id, Model model) {
        StorageResponseDTO storage = storageService.getStorageById(id);
        model.addAttribute("pageTitle", "입고 상세 조회 - " + id);
        model.addAttribute("storage", storage);
        return "storages/detail";
    }

    //  입고 수정 폼
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        StorageResponseDTO storage = storageService.getStorageById(id);
        model.addAttribute("storage", storage);
        model.addAttribute("statusList", StorageStatus.values());
        return "storages/update";
    }

    // 입고 수정 처리
    @PostMapping("/update/{id}")
    public String updateStorage(@PathVariable int id, @ModelAttribute("storage") StorageRequestDTO storageRequestDTO) {
        storageService.updateStorage(id, storageRequestDTO);
        return "redirect:/storages";
    }

    //  입고 삭제
    @PostMapping("/delete/{id}")
    public String deleteStorage(@PathVariable int id) {
        storageService.deleteStorage(id);
        return "redirect:/storages";
    }

    //  중복 발주 ID 검사 (AJAX)
    @PostMapping("/check-duplicate")
    @ResponseBody
    public boolean checkDuplicatePurchaseId(@RequestParam("purchaseId") Integer purchaseId) {
        return storageService.isDuplicatePurchaseId(purchaseId);
    }

    //  발주 ID로 발주 상세 정보 조회 (AJAX)
    @GetMapping("/purchase-info/{purchaseId}")
    @ResponseBody
    public ResponseEntity<PurchaseInfoResponseDTO> getPurchaseInfo(@PathVariable Integer purchaseId) {
        PurchaseInfoResponseDTO dto = storageService.getPurchaseInfoById(purchaseId);
        return ResponseEntity.ok(dto);
    }
}
