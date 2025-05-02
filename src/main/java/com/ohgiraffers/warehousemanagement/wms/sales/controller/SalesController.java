package com.ohgiraffers.warehousemanagement.wms.sales.controller;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.common.exception.InventoryNotFoundException;
import com.ohgiraffers.warehousemanagement.wms.common.exception.ProductNotFoundException;
import com.ohgiraffers.warehousemanagement.wms.inventory.model.DTO.InventoryViewDTO;
import com.ohgiraffers.warehousemanagement.wms.sales.model.dto.SalesDTO;
import com.ohgiraffers.warehousemanagement.wms.sales.model.entity.SalesStatus;
import com.ohgiraffers.warehousemanagement.wms.sales.service.SalesServiceImpl;
import com.ohgiraffers.warehousemanagement.wms.store.model.dto.StoreDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sales")
@Validated
public class SalesController {

    private final SalesServiceImpl salesServiceImpl;

    @Autowired
    public SalesController(SalesServiceImpl salesServiceImpl) {
        this.salesServiceImpl = salesServiceImpl;
    }

    @GetMapping
    public ModelAndView getAllSales(ModelAndView mv) {
        List<SalesDTO> salesLists = salesServiceImpl.getAllSales();

        if (salesLists != null) {
            mv.addObject("salesLists", salesLists);
            mv.setViewName("sales/sales");
        } else { // 아무것도 없으면 빈 리스트
            mv.addObject("salesLists", new ArrayList<>());
        }

        return mv;
    }
    
    @GetMapping("/create")
    public String createSales() {
        return "sales/create";
    }

    // 새로고침 시 중복 등록 방지 및 이동 시 URL을 맞춰주기 위해 forward 말고 redirect 사용했음!
    @PostMapping("/create")
    public String createSales(@Valid @ModelAttribute SalesDTO salesDTO, RedirectAttributes rdtat) {
        AuthDetails authDetails = (AuthDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authDetails.getUserId();
        String resultUrl = null;

        try {
            int salesId = salesServiceImpl.createSales(salesDTO, userId);
            rdtat.addFlashAttribute("salesDTO", salesId);
            rdtat.addFlashAttribute("message", "수주서가 등록되었습니다.");
            resultUrl = "redirect:/sales/" + salesId;
        } catch (ProductNotFoundException | InventoryNotFoundException e) {
            rdtat.addFlashAttribute("error",e.getMessage());
            resultUrl = "redirect:/sales/create";
        }

        return resultUrl;
    }

    @GetMapping("/{salesId}")
    public ModelAndView getSalesById(@PathVariable(name = "salesId") Integer salesId, ModelAndView mv, RedirectAttributes rdtat) {
        SalesDTO findDTO = salesServiceImpl.getSalesById(salesId);
        System.out.println("findDTO"+findDTO);
        if (findDTO != null) {
            mv.addObject("sales", findDTO);
            mv.setViewName("/sales/detail"); // view 수정필요 상세페이지로 가야됨
        } else {
            rdtat.addFlashAttribute("message","수주 데이터를 찾을 수 없습니다.");
            mv.setViewName("redirect:/sales"); // 없으면 목록으로 돌아감
        }

        return mv;
    }

    // 수주서 수정 화면 (id 기준으로 기존 데이터 가져와서 띄워줌)
    @GetMapping("/update/{salesId}")
    public ModelAndView updateSales(@PathVariable Integer salesId, ModelAndView mv, RedirectAttributes rdtat) {

        SalesDTO findDTO = salesServiceImpl.getSalesById(salesId);

        if (findDTO != null) {
            mv.addObject("salesDTO", findDTO);
            mv.setViewName("sales/update"); // view url 확인필요
        } else {
            rdtat.addFlashAttribute("message","수주 데이터를 찾을 수 없습니다.");
            mv.setViewName("redirect:/sales");
        }

        return mv;
    }

    // 수주서 수정
    @PatchMapping("/update/{salesId}")
    public String updateSales(@PathVariable Integer salesId, @Valid @ModelAttribute SalesDTO salesDTO, RedirectAttributes rdtat) {
        SalesDTO updatedDTO = salesServiceImpl.updateSales(salesId, salesDTO);
        String resultUrl = null;

        if (updatedDTO != null) {
            rdtat.addFlashAttribute("sales", updatedDTO);
            rdtat.addFlashAttribute("message","수주서를 수정했습니다.");
            resultUrl = "redirect:/sales/" + salesId;
        } else {
           rdtat.addFlashAttribute("message", "수주서 수정에 실패했습니다. 다시 시도해주세요.");
           resultUrl = "redirect:/sales/" + salesId;
        }
        return resultUrl;
    }

    // 수주 상태 변경
    @PatchMapping("/update/status/{salesId}")
    public String updateStatusSales(@PathVariable Integer salesId, @RequestParam(name = "status") SalesStatus status, RedirectAttributes rdtat) {
        boolean result = salesServiceImpl.updateStatusSales(salesId, status);
        String resultUrl = null;

        if (result) {
            rdtat.addFlashAttribute("message", "수주 상태가 변경되었습니다.");
            resultUrl = "redirect:/sales/" + salesId;
        } else {
            rdtat.addFlashAttribute("message","상태 변경에 실패했습니다. 다시 시도해주세요.");
            resultUrl = "redirect:/sales/" + salesId;
        }

        return resultUrl;
    }

    // 점포명 검색하기(한글자만 입력해도 포함된거 다 가져옴)
    @GetMapping("/search/stores")
    @ResponseBody
    public List<StoreDTO> searchStores(@RequestParam(name = "storeSearchName") String storeName) {
        List<StoreDTO> searchStoreResults = salesServiceImpl.searchStoresByName(storeName);
        return searchStoreResults;
    }

    /*// 상품 검색하기(재고에서 조회하는거라 상품명 입력하면 총 재고 가져옴
    @GetMapping("/search/products")
    @ResponseBody
    public List<InventoryViewDTO> searchProducts(@RequestParam(name = "productSearchName") String productName) {
        List<InventoryViewDTO> searchProductResults = salesServiceImpl.searchProductsByName(productName);
        return searchProductResults;
    }

    // 담당자 검색하기
    @GetMapping("/search/users")
    @ResponseBody
    public List<UserDTO> searchUsers(@RequestParam(name = "userSearchName") String userName) {
        List<UserDTO> searchUsersResults = salesServiceImpl.searchUsersByName(userName);
        return searchUsersResults;
    }*/
}
