package com.ohgiraffers.warehousemanagement.wms.product.controller;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 관리를 위한 컨트롤러 클래스
 * 상품 목록 조회, 생성, 수정, 삭제 등의 기능을 제공합니다.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
    private final ProductService productService;

    /**
     * 생성자를 통한 의존성 주입
     * @param productServiceImpl 상품 서비스 구현체
     * @param productService 상품 서비스 인터페이스
     */
    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl, ProductService productService) {
        this.productServiceImpl = productServiceImpl;
        this.productService = productService;
    }

    /**
     * 모든 상품 목록을 페이징하여 조회하는 메서드
     * @param page 현재 페이지 (기본값: 1)
     * @param pageSize 페이지당 항목 수 (기본값: 10)
     * @param searchKeyword 검색 키워드 (선택 사항)
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 상품 목록 페이지 뷰 이름
     */
    @GetMapping
    public String getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchKeyword,
            Model model) {
        // 상품 목록과 페이징 정보를 서비스로부터 가져옴
        ProductPageResponseDTO productPage = productServiceImpl.getAllProducts(page, pageSize, searchKeyword);
        
        // 모델에 데이터 추가
        model.addAttribute("products", productPage.getProducts());
        model.addAttribute("currentPage", productPage.getCurrentPage());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalItems());
        model.addAttribute("startItem", productPage.getStartItem());
        model.addAttribute("endItem", productPage.getEndItem());
        model.addAttribute("searchKeyword", searchKeyword);
        
        // 페이지 타이틀과 카드 정보 설정
        model.addAttribute("pageTitle", "상품 관리");
        model.addAttribute("cardTitle", "상품 목록");
        model.addAttribute("cardDescription", "등록된 모든 상품을 확인하고 관리할 수 있습니다.");
        
        return "products/list";
    }

    /**
     * 상품 생성 폼을 제공하는 메서드
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 상품 생성 폼 페이지 뷰 이름
     */
    @GetMapping("/create")
    public String createProductForm(Model model) {
        // 현재 인증된 사용자의 ID를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
            userId = ((AuthDetails) authentication.getPrincipal()).getUserId();
        }
        
        // 새 상품 생성 DTO 생성 및 사용자 ID 설정
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setUserId(userId);
        
        // 모델에 데이터 추가 (상품 DTO, 카테고리 목록, 공급업체 목록)
        model.addAttribute("product", productCreateDTO);
        model.addAttribute("categories", productServiceImpl.getCategories());
        model.addAttribute("suppliers", productService.getSuppliers());
        
        return "products/create";
    }

    /**
     * 상품을 생성하는 메서드
     * @param productCreateDTO 생성할 상품 정보를 담은 DTO
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 성공 시 상품 목록 페이지로 리다이렉트, 실패 시 생성 폼 페이지
     */
    @PostMapping("/create")
    public String createProduct(@ModelAttribute("product") ProductCreateDTO productCreateDTO, Model model) {
        try {
            // 현재 인증된 사용자의 ID를 가져와 DTO에 설정
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
                productCreateDTO.setUserId(((AuthDetails) authentication.getPrincipal()).getUserId());
            }
            
            // 상품 생성 서비스 호출
            productServiceImpl.createProduct(productCreateDTO);
            
            // 상품 목록 페이지로 리다이렉트
            return "redirect:/products";
        } catch (IllegalArgumentException e) {
            // 유효성 검사 실패 등의 예외 발생 시 에러 메시지와 함께 생성 폼으로 돌아감
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", productServiceImpl.getCategories());
            model.addAttribute("suppliers", productService.getSuppliers());
            return "products/create";
        }
    }

    /**
     * 상품 수정 폼을 제공하는 메서드
     * @param id 수정할 상품의 ID
     * @param model 뷰에 전달할 데이터를 담는 Model 객체
     * @return 상품 수정 폼 페이지 뷰 이름
     */
    @GetMapping("/{id}")
    public String updateProductForm(@PathVariable("id") Integer id, Model model) {
        // ID로 상품 정보 조회
        ProductResponseDTO product = productService.getProductById(id);
        
        // 현재 인증된 사용자의 ID를 상품 정보에 설정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
            product.setUserId(((AuthDetails) authentication.getPrincipal()).getUserId());
        }
        
        // 모델에 데이터 추가 (상품 정보, 카테고리 목록, 공급업체 목록)
        model.addAttribute("product", product);
        model.addAttribute("categories", productServiceImpl.getCategories());
        model.addAttribute("suppliers", productService.getSuppliers());
        
        return "products/update";
    }

    /**
     * 상품을 수정하는 메서드
     * @param id 수정할 상품의 ID
     * @param productCreateDTO 수정할 상품 정보를 담은 DTO
     * @return 상품 목록 페이지로 리다이렉트
     */
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Integer id, @ModelAttribute("product") ProductCreateDTO productCreateDTO) {
        // 현재 인증된 사용자의 ID를 DTO에 설정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
            productCreateDTO.setUserId(((AuthDetails) authentication.getPrincipal()).getUserId());
        }
        
        // 상품 수정 서비스 호출
        productServiceImpl.updateProduct(id, productCreateDTO);
        
        // 상품 목록 페이지로 리다이렉트
        return "redirect:/products";
    }

    /**
     * 상품을 삭제하는 메서드
     * @param id 삭제할 상품의 ID
     * @return 상품 목록 페이지로 리다이렉트
     */
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        // 상품 삭제 서비스 호출
        productServiceImpl.deleteProduct(id);
        
        // 상품 목록 페이지로 리다이렉트
        return "redirect:/products";
    }
}