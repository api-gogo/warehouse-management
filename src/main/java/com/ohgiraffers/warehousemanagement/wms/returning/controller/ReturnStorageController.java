package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import com.ohgiraffers.warehousemanagement.wms.returning.model.DTO.ReturnStorageDTO;
import com.ohgiraffers.warehousemanagement.wms.returning.service.ReturnStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/returns/inbound")
@Validated
public class ReturnStorageController {

    private final ReturnStorageService returnStorageService;

    @Autowired
    public ReturnStorageController(ReturnStorageService returnStorageService) {
        this.returnStorageService = returnStorageService;
    }

    @GetMapping("/list")
    public ModelAndView getAllReturns(ModelAndView mv) {
        List<ReturnStorageDTO> returnLists = returnStorageService.getAllReturns();

        if (returnLists != null) {
            mv.addObject("returnLists", returnLists);
            mv.setViewName("returns/inbound/list");
        } else {
            mv.addObject("returnLists", new ArrayList<>());
            mv.setViewName("returns/inbound/list");
        }
        return mv;
    }

    @GetMapping("/create")
    public String registView() {
        return "returns/inbound/create";
    }

    @PostMapping("/create")
    public String createReturns(@Valid @ModelAttribute ReturnStorageDTO returnStorageDTO, RedirectAttributes rdtat) {
        System.out.println("returnStorageDTO : " + returnStorageDTO);
        int returnId = returnStorageService.createReturns(returnStorageDTO);
        String resultUrl = null;

        if (returnId > 0) {
            rdtat.addFlashAttribute("returnDTO", returnId);
            rdtat.addFlashAttribute("message", "반품서가 등록되었습니다.");
            resultUrl = "redirect:/returns/inbound/list"; // 성공시 목록 화면
        } else {
            rdtat.addFlashAttribute("message", "반품서 등록에 실패하였습니다. 다시 시도해주세요");
            resultUrl = "redirect:/returns/inbound/create";
        }
        return resultUrl;
    }

    @GetMapping("/detail/{returnStorageId}")
    public ModelAndView getReturnsById(@PathVariable(name = "returnStorageId") Integer returnStorageId,
                                       ModelAndView mv, RedirectAttributes rdtat) {
        ReturnStorageDTO findDTO = returnStorageService.getReturnsById(returnStorageId);
        if (findDTO != null) {
            mv.addObject("returnsStorage", findDTO);
            mv.setViewName("returns/inbound/detail");
        } else {
            rdtat.addFlashAttribute("message", "반품 데이터를 찾을 수 없습니다.");
            mv.setViewName("redirect:/returns/inbound/list");
        }
        return mv;
    }

    @GetMapping("/delete/{returnStorageId}")
    public String deleteReturns(@PathVariable(name = "returnStorageId") Integer returnStorageId,
                                RedirectAttributes redirectAttributes) {
        // 이 메서드는 서비스 메서드가 구현되지 않았으므로 리다이렉트만 수행
        redirectAttributes.addFlashAttribute("message", "이 기능은 아직 구현되지 않았습니다.");
        return "redirect:/returns/inbound/list";
    }

    @GetMapping("/update/{returnStorageId}")
    public ModelAndView updateReturnsById(@PathVariable("returnStorageId") Integer returnStorageId,
                                          ModelAndView mv, RedirectAttributes rdtat) {
        ReturnStorageDTO findDTO = returnStorageService.getReturnsById(returnStorageId);

        if (findDTO != null) {
            mv.addObject("returnsStorage", findDTO);
            mv.setViewName("returns/inbound/update");
        } else {
            rdtat.addFlashAttribute("message", "반품 데이터를 찾을 수 없습니다.");
            mv.setViewName("redirect:/returns/inbound/list");
        }

        return mv;
    }
}