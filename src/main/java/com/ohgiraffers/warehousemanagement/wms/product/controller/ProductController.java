package com.ohgiraffers.warehousemanagement.wms.product.controller;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductCreateDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductPageResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.model.DTO.ProductResponseDTO;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductService;
import com.ohgiraffers.warehousemanagement.wms.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

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

    @GetMapping
    public String getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false, defaultValue = "all") String statusFilter,
            Model model) {
        ProductPageResponseDTO productPage = productServiceImpl.getAllProducts(page, pageSize, searchKeyword);
        model.addAttribute("products", productPage.getProducts());
        model.addAttribute("currentPage", productPage.getCurrentPage());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalItems());
        model.addAttribute("startItem", productPage.getStartItem());
        model.addAttribute("endItem", productPage.getEndItem());
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("pageTitle", "상품 관리");
        model.addAttribute("cardTitle", "상품 목록");
        model.addAttribute("cardDescription", "등록된 모든 상품을 확인하고 관리할 수 있습니다.");
        return "products/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('상품_사원', '상품_매니저', '상품_관리자', '통합_관리자')")
    public String createProductForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
            userId = ((AuthDetails) authentication.getPrincipal()).getUserId();
        }
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setUserId(userId);
        model.addAttribute("product", productCreateDTO);
        model.addAttribute("categories", productServiceImpl.getCategories());
        model.addAttribute("suppliers", productService.getSuppliers());
        return "products/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('상품_사원', '상품_매니저', '상품_관리자', '통합_관리자')")
    public String createProduct(@ModelAttribute("product") ProductCreateDTO productCreateDTO, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
                productCreateDTO.setUserId(((AuthDetails) authentication.getPrincipal()).getUserId());
                if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("상품_매니저") || a.getAuthority().equals("상품_관리자") || a.getAuthority().equals("통합_관리자"))) {
                    productServiceImpl.create(productCreateDTO); // 매니저/관리자는 즉시 등록
                } else {
                    productServiceImpl.createPending(productCreateDTO); // 사원은 대기 상태로 등록
                }
            }
            return "redirect:/products?t=" + System.currentTimeMillis();
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", productServiceImpl.getCategories());
            model.addAttribute("suppliers", productService.getSuppliers());
            return "products/create";
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('상품_사원', '상품_매니저', '상품_관리자', '통합_관리자')")
    public String updateProductForm(@PathVariable("id") Integer id, Model model) {
        ProductResponseDTO product = productService.getProductById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
            product.setUserId(((AuthDetails) authentication.getPrincipal()).getUserId());
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", productServiceImpl.getCategories());
        model.addAttribute("suppliers", productService.getSuppliers());
        return "products/update";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('상품_사원', '상품_매니저', '상품_관리자', '통합_관리자')")
    public String updateProduct(@PathVariable("id") Integer id, @ModelAttribute("product") ProductCreateDTO productCreateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
            productCreateDTO.setUserId(((AuthDetails) authentication.getPrincipal()).getUserId());
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("상품_매니저") || a.getAuthority().equals("상품_관리자") || a.getAuthority().equals("통합_관리자"))) {
                productServiceImpl.update(id, productCreateDTO); // 매니저/관리자는 즉시 수정
            } else {
                productServiceImpl.updatePending(id, productCreateDTO); // 사원은 대기 상태로 수정
            }
        }
        return "redirect:/products?t=" + System.currentTimeMillis();
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('상품_사원', '상품_매니저', '상품_관리자', '통합_관리자')")
    public String deleteProduct(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthDetails) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("상품_매니저") || a.getAuthority().equals("상품_관리자") || a.getAuthority().equals("통합_관리자"))) {
                productServiceImpl.delete(id); // 매니저/관리자는 즉시 삭제
            } else {
                productServiceImpl.deletePending(id); // 사원은 대기 상태로 삭제
            }
        }
        return "redirect:/products?t=" + System.currentTimeMillis();
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('상품_매니저', '상품_관리자', '통합_관리자')")
    public Object getPendingProducts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("상품_사원"))) {
            return new RedirectView("/products?t=" + System.currentTimeMillis()); // 사원 접근 시 리다이렉트
        }
        List<ProductResponseDTO> pendingProducts = productService.findPending();
        model.addAttribute("products", pendingProducts);
        model.addAttribute("pageTitle", "대기 상품 관리");
        model.addAttribute("cardTitle", "대기 상품 목록");
        model.addAttribute("cardDescription", "승인 대기 중인 상품을 확인하고 관리할 수 있습니다.");
        return "products/pending";
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAnyAuthority('상품_매니저', '상품_관리자', '통합_관리자')")
    public Object approveProduct(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("상품_사원"))) {
            return new RedirectView("/products?t=" + System.currentTimeMillis()); // 사원 접근 시 리다이렉트
        }
        productServiceImpl.approveProduct(id);
        return "redirect:/products?t=" + System.currentTimeMillis();
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasAnyAuthority('상품_매니저', '상품_관리자', '통합_관리자')")
    public Object rejectProduct(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("상품_사원"))) {
            return new RedirectView("/products?t=" + System.currentTimeMillis()); // 사원 접근 시 리다이렉트
        }
        productServiceImpl.rejectProduct(id);
        return "redirect:/products?t=" + System.currentTimeMillis();
    }

    @PostMapping("/restore/{id}")
    @PreAuthorize("hasAnyAuthority('상품_관리자', '통합_관리자')")
    public String restoreProduct(@PathVariable("id") Integer id) {
        productServiceImpl.restoreProduct(id);
        return "redirect:/products?t=" + System.currentTimeMillis();
    }
}