package com.ohgiraffers.warehousemanagement.wms.product.controller;

import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 목록 조회
    @GetMapping({"", "/list"})
    public String getAllProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            Model model) {
        int pageSize = 10;
        ProductPageResponseDTO productPage = productService.getAllProducts(page, pageSize, searchKeyword);

        model.addAttribute("pageTitle", "상품 관리");
        model.addAttribute("cardTitle", "상품 목록");
        model.addAttribute("cardDescription", "등록된 모든 상품을 확인하고 관리할 수 있습니다.");
        model.addAttribute("products", productPage.getProducts());
        model.addAttribute("currentPage", productPage.getCurrentPage());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalItems());
        model.addAttribute("startItem", productPage.getStartItem());
        model.addAttribute("endItem", productPage.getEndItem());
        model.addAttribute("searchKeyword", searchKeyword);

        return "product/list";
    }

    // 특정 상품 조회
    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") Integer id, Model model) {
        ProductResponseDTO product = productService.getProductById(id);
        model.addAttribute("pageTitle", "상품 편집 - " + product.getProductName());
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.getMockCategories());
        return "product/edit";
    }

    // 신규 상품 등록 폼 표시
    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("pageTitle", "신규 상품 등록");
        model.addAttribute("product", new ProductCreateDTO());
        model.addAttribute("categories", productService.getMockCategories());
        return "product/new";
    }

    // 상품 생성
    @PostMapping("/add")
    public String createProduct(@ModelAttribute ProductCreateDTO createDTO, Model model) {
        try {
            productService.createProduct(createDTO);
            return "redirect:/product";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "신규 상품 등록");
            model.addAttribute("product", createDTO);
            model.addAttribute("categories", productService.getMockCategories());
            return "product/new";
        }
    }

    // 상품 수정
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Integer id, @ModelAttribute ProductCreateDTO updateDTO, Model model) {
        try {
            productService.updateProduct(id, updateDTO);
            return "redirect:/product";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "상품 편집 - " + updateDTO.getProductName());
            model.addAttribute("product", updateDTO);
            model.addAttribute("categories", productService.getMockCategories());
            return "product/edit";
        }
    }

    // 상품 삭제
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }

    // 상품 내보내기 (임시 리다이렉트)
    @GetMapping("/export")
    public String exportProducts() {
        return "redirect:/product";
    }
}