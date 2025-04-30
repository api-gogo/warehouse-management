package com.ohgiraffers.warehousemanagement.wms.store.controller;

import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;
import com.ohgiraffers.warehousemanagement.wms.store.service.StoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stores")
public class StoreController {

    private final StoreServiceImpl storeServiceImpl;

    @Autowired
    public StoreController(StoreServiceImpl storeServiceImpl) {
        this.storeServiceImpl = storeServiceImpl;
    }

    @GetMapping
    public String listStores(@RequestParam(required = false) String search,
                           @RequestParam(required = false, defaultValue = "active") String status,
                           @PageableDefault(size = 10) Pageable pageable,
                           Model model) {

        if (search != null && search.trim().isEmpty()) {
            search = null;
        }

        Page<StoreDTO> storePage = storeServiceImpl.findAll(search, status, pageable);
        if (storePage.isEmpty()) {
            model.addAttribute("message", "조건에 맞는 점포가 없습니다.");
        }

        model.addAttribute("stores", storePage.getContent());
        model.addAttribute("currentPage", storePage.getNumber());
        model.addAttribute("totalPages", storePage.getTotalPages());
        model.addAttribute("size", storePage.getSize());

        model.addAttribute("search", search);
        model.addAttribute("status", status);

        return "stores/list";
    }

    @GetMapping("/{storeId}")
    public String showStoreDetail(@PathVariable Integer storeId, Model model) {
        StoreDTO storeDTO = storeServiceImpl.findById(storeId);

        if (storeDTO == null) {
            String message = null;
            message = "해당 id의 점포가 없습니다.";
            model.addAttribute("message", message);
        }

        model.addAttribute("store", storeDTO);
        return "stores/detail";
    }

    @GetMapping("/create")
    public String showStoreForm(Model model) {
        StoreDTO storeDTO = new StoreDTO();
        model.addAttribute("store", storeDTO);
        return "stores/create";
    }

    @PostMapping("/create")
    public String registerStore(@ModelAttribute StoreDTO storeDTO, RedirectAttributes redirectAttributes) {

        Integer result = storeServiceImpl.createStore(storeDTO);
        String message = null;

        if (result == -1) {
            message = "중복된 점포 이름이 존재합니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/stores/create";
        } else if (result == -2) {
            message = "중복된 담당자 전화번호가 존재합니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/stores/create";
        } else if (result == -3) {
            message = "중복된 담당자 이메일이 존재합니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/stores/create";
        } else if (result == 0) {
            message = "서버에 오류가 발생하였습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/stores/create";
        } else {
            message = "점포 추가가 완료되었습니다.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/stores";
    }

    @GetMapping("/{storeId}/edit")
    public String showStoreEditForm(@PathVariable Integer storeId, RedirectAttributes redirectAttributes) {
        StoreDTO storeDTO = storeServiceImpl.findById(storeId);

        if (storeDTO == null) {
            String message = null;
            message = "해당 id의 거래처가 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/stores/" + storeId;
        }

        redirectAttributes.addFlashAttribute("storeDTO", storeDTO);
        return "stores/edit";
    }

    @PatchMapping("/{storeId}")
    public String updateStore(@PathVariable Integer storeId,
                                 StoreDTO storeDTO, RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = storeServiceImpl.updateStore(storeId, storeDTO);

        if (!result) {
            message = "거래처 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/stores";
        }

        message = "거래처 정보가 업데이트 되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/stores/" + storeId;
    }

    @PostMapping("/{storeId}/delete")
    public String deleteStore(@PathVariable Integer storeId, RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = storeServiceImpl.deleteStore(storeId);

        if (!result) {
            message = "거래처 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/stores";
        }

        message = "거래처가 삭제 되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/stores";
    }
}