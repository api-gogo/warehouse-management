package com.ohgiraffers.warehousemanagement.wms.category.controller;

import com.ohgiraffers.warehousemanagement.wms.category.model.DTO.CategoryDTO;
import com.ohgiraffers.warehousemanagement.wms.category.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 카테고리 목록 조회
    @GetMapping("")
    public String getAllCategories(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "level", required = false) Integer level,
            Model model) {
        int pageSize = 10;
        Map<String, Object> categoryPage = categoryService.getAllCategories(page, pageSize, searchKeyword, level);

        List<CategoryDTO> parentCategories = categoryService.getParentCategories();
        logger.info("Parent categories size: {}", parentCategories.size());
        parentCategories.forEach(parent -> logger.info("Parent category: {}", parent));

        logger.info("Total items: {}, Total pages: {}", categoryPage.get("totalItems"), categoryPage.get("totalPages"));

        model.addAttribute("categories", categoryPage.get("categories"));
        model.addAttribute("currentPage", categoryPage.get("currentPage"));
        model.addAttribute("totalPages", categoryPage.get("totalPages"));
        model.addAttribute("totalItems", categoryPage.get("totalItems"));
        model.addAttribute("startPage", categoryPage.get("startPage"));
        model.addAttribute("endPage", categoryPage.get("endPage"));
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("level", level);
        model.addAttribute("parentCategoryMap", categoryService.getParentCategoryMap());
        model.addAttribute("parentCategories", parentCategories);

        return "categories/list";
    }

    // 카테고리 생성 (모달창에서 제출)
    @PostMapping("/create")
    public String createCategory(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        return "redirect:/categories";
    }

    // 카테고리 수정 (모달창에서 제출)
    @PostMapping("/update")
    public String updateCategory(@ModelAttribute CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO.getCategoryId().intValue(), categoryDTO);
        return "redirect:/categories";
    }

    // 카테고리 삭제
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("categoryId") Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}