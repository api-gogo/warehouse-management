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

@Controller
@RequestMapping("/storage")
public class StorageController {

    private static final Logger log = LoggerFactory.getLogger(StorageController.class);
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    // ✅ 입고 목록 조회
    @GetMapping("/list")
    public String getAllStorage(Model model) {
        model.addAttribute("storages", storageService.getAllStorage()); // Key 값: storages
        return "storage/list"; // templates/storage/list.html
    }

    // ✅ 입고 등록 화면
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("storageRequestDTO", new StorageRequestDTO());
        model.addAttribute("statusList", StorageStatus.values());
        return "storage/create"; // templates/storage/create.html
    }

    // ✅ 입고 등록 처리
    @PostMapping
    public String createStorage(@ModelAttribute @Valid StorageRequestDTO storageRequestDTO) {
        log.info("입고 등록 요청: 데이터={}", storageRequestDTO);

        if (storageRequestDTO.getPurchaseId() != null) {
            log.info("발주 ID 제공됨: {}", storageRequestDTO.getPurchaseId());
        }

        storageService.createStorage(storageRequestDTO);
        return "redirect:/storage/list"; // 등록 후 목록 페이지로 이동
    }

    // ✅ 입고 상세 조회
    @GetMapping("/{id}")
    public String getStorageById(@PathVariable("id") int id, Model model) {
        log.info("입고 상세 조회 요청: ID={}", id);
        StorageResponseDTO storage = storageService.getStorageById(id);
        model.addAttribute("pageTitle", "입고 상세 조회 - " + id);
        model.addAttribute("storage", storage);
        return "storage/detail"; // templates/storage/detail.html
    }

    // ✅ 입고 수정 처리
    @PostMapping("/{id}")
    public String updateStorage(@PathVariable("id") int id,
                                @ModelAttribute @Valid StorageRequestDTO storageRequestDTO) {
        log.info("입고 수정 요청: ID={}, 데이터={}", id, storageRequestDTO);
        storageService.updateStorage(id, storageRequestDTO);
        return "redirect:/storage/list"; // 수정 후 목록 이동
    }

    // ✅ 입고 수정 화면 보여주기
    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        log.info("입고 수정 폼 요청: ID={}", id);
        StorageResponseDTO storage = storageService.getStorageById(id);
        model.addAttribute("storage", storage);
        model.addAttribute("statusList", StorageStatus.values());
        return "storage/edit";  // templates/storage/edit.html
    }


    // ✅ 입고 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteStorage(@PathVariable("id") int id) {
        log.info("입고 삭제 요청: ID={}", id);
        storageService.deleteStorage(id);
        return "redirect:/storage/list"; // 삭제 후 목록 이동
    }
}

