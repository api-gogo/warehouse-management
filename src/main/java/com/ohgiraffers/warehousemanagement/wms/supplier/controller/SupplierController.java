package com.ohgiraffers.warehousemanagement.wms.supplier.controller;

import com.ohgiraffers.warehousemanagement.wms.supplier.model.dto.SupplierDTO;
import com.ohgiraffers.warehousemanagement.wms.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller()
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String getSuppliers(@RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "active") String status,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        if (search != null && search.trim().isEmpty()) {
            search = null;
        }

        Page<SupplierDTO> supplierPage = supplierService.findSuppliers(search, status, pageable);
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
}
