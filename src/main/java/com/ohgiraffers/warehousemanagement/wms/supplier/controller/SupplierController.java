package com.ohgiraffers.warehousemanagement.wms.supplier.controller;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;
import com.ohgiraffers.warehousemanagement.wms.supplier.service.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller()
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierServiceImpl supplierServiceImpl;

    @Autowired
    public SupplierController(SupplierServiceImpl supplierServiceImpl) {
        this.supplierServiceImpl = supplierServiceImpl;
    }

    @GetMapping
    public String listSuppliers(@RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "active") String status,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        if (search != null && search.trim().isEmpty()) {
            search = null;
        }

        Page<SupplierDTO> supplierPage = supplierServiceImpl.findAll(search, status, pageable);
        if (supplierPage.isEmpty()) {
            model.addAttribute("message", "조건에 맞는 거래처가 없습니다.");
        }

        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", supplierPage.getNumber());
        model.addAttribute("totalPages", supplierPage.getTotalPages());
        model.addAttribute("size", supplierPage.getSize());

        model.addAttribute("search", search);
        model.addAttribute("status", status);

        return "suppliers/list";
    }

    @GetMapping("/{supplierId}")
    public String showSupplierDetail(@PathVariable Integer supplierId, Model model) {
        SupplierDTO supplierDTO = supplierServiceImpl.findById(supplierId);

        if (supplierDTO == null) {
            String message = null;
            message = "해당 id의 거래처가 없습니다.";
            model.addAttribute("message", message);
        }

        model.addAttribute("supplier", supplierDTO);
        return "suppliers/detail";
    }

    @GetMapping("/create")
    public String showSupplierForm(Model model) {
        SupplierDTO supplierDTO = new SupplierDTO();
        model.addAttribute("supplier", supplierDTO);
        return "suppliers/create";
    }

    @PostMapping("/create")
    public String registerSupplier(@ModelAttribute SupplierDTO supplierDTO, RedirectAttributes redirectAttributes) {

        Integer result = supplierServiceImpl.createSupplier(supplierDTO);
        String message = null;

        if (result == -1) {
            message = "중복된 거래처 이름이 존재합니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/suppliers/create";
        } else if (result == -2) {
            message = "중복된 담당자 전화번호가 존재합니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/suppliers/create";
        } else if (result == -3) {
            message = "중복된 담당자 이메일이 존재합니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/suppliers/create";
        } else if (result == 0) {
            message = "서버에 오류가 발생하였습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/suppliers/create";
        } else {
            message = "거래처 추가가 완료되었습니다.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/suppliers";
    }
}