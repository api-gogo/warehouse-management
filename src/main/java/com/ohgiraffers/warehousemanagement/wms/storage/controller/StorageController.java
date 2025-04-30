// ✅ StorageController.java
package com.ohgiraffers.warehousemanagement.wms.storage.controller;

import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.request.StorageRequestDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.DTO.response.StorageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.storage.model.StorageStatus;
import com.ohgiraffers.warehousemanagement.wms.storage.service.StorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    //  목록 조회 + 검색
    @GetMapping
    public String getAllStorage(@RequestParam(required = false) String searchKeyword, Model model) {
        List<StorageResponseDTO> storages = (searchKeyword != null && !searchKeyword.isBlank())
                ? storageService.searchStoragesByPurchaseId(searchKeyword)
                : storageService.getAllStorage();
        model.addAttribute("storages", storages);
        model.addAttribute("searchKeyword", searchKeyword);
        return "storages/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("storages", new StorageRequestDTO());
        model.addAttribute("statusList", StorageStatus.values());
        return "storages/create";
    }

    @PostMapping
    public String createStorage(@ModelAttribute @Valid StorageRequestDTO storageRequestDTO) {
        log.info("입고 등록 요청: {}", storageRequestDTO);
        StorageResponseDTO created = storageService.createStorage(storageRequestDTO);
        log.info("등록 완료 - 입고 ID: {}", created.getStorageId());
        return "redirect:/storages";
    }

    @GetMapping("/{id}")
    public String getStorageById(@PathVariable int id, Model model) {
        StorageResponseDTO storage = storageService.getStorageById(id);
        model.addAttribute("pageTitle", "입고 상세 조회 - " + id);
        model.addAttribute("storage", storage);
        return "storages/detail";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        StorageResponseDTO storage = storageService.getStorageById(id);
        model.addAttribute("storage", storage);
        model.addAttribute("statusList", StorageStatus.values());
        return "storages/update";
    }

    @PostMapping("/update/{id}")
    public String updateStorage(@PathVariable int id, @ModelAttribute("storage") StorageRequestDTO storageRequestDTO) {
        storageService.updateStorage(id, storageRequestDTO);
        return "redirect:/storages";
    }

    @PostMapping("/delete/{id}")
    public String deleteStorage(@PathVariable int id) {
        storageService.deleteStorage(id);
        return "redirect:/storages";
    }
}
