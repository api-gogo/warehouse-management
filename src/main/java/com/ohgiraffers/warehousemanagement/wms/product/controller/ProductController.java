package com.ohgiraffers.warehousemanagement.wms.product.controller;

import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
    private final ProductService productService;

    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl, ProductService productService) {
        this.productServiceImpl = productServiceImpl;
        this.productService = productService;
    }

    // 상품 목록 조회
    @GetMapping
    public String getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchKeyword,
            Model model) {
        ProductPageResponseDTO productPage = productServiceImpl.getAllProducts(page, pageSize, searchKeyword);
        model.addAttribute("products", productPage.getProducts());
        model.addAttribute("currentPage", productPage.getCurrentPage());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalItems());
        model.addAttribute("startItem", productPage.getStartItem());
        model.addAttribute("endItem", productPage.getEndItem());
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("pageTitle", "상품 관리");
        model.addAttribute("cardTitle", "상품 목록");
        model.addAttribute("cardDescription", "등록된 모든 상품을 확인하고 관리할 수 있습니다.");
        return "products/list";
    }

    // 상품 생성 페이지
    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ProductCreateDTO());
        model.addAttribute("categories", productServiceImpl.getCategories());
        return "products/create";
    }

    // 상품 생성
    @PostMapping("/create")
    public String createProduct(@ModelAttribute("product") ProductCreateDTO productCreateDTO, Model model) {
        try {
            productServiceImpl.createProduct(productCreateDTO);
            return "redirect:/products";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", productServiceImpl.getCategories());
            return "products/create";
        }
    }

    // 상품 수정 페이지
    @GetMapping("/{id}")
    public String updateProductForm(@PathVariable("id") Integer id, Model model) {
        ProductResponseDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", productServiceImpl.getCategories());
        return "products/update";
    }

    // 상품 수정
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Integer id, @ModelAttribute("product") ProductCreateDTO productCreateDTO) {
        productServiceImpl.updateProduct(id, productCreateDTO);
        return "redirect:/products";
    }

    // 상품 삭제
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productServiceImpl.deleteProduct(id);
        return "redirect:/products";
    }
}