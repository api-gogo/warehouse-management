package com.ohgiraffers.warehousemanagement.wms.sales.controller;

import com.ohgiraffers.warehousemanagement.wms.sales.model.dto.SalesDTO;
import com.ohgiraffers.warehousemanagement.wms.sales.service.SalesServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            mv.setViewName("sales");
        } else { // 아무것도 없으면 빈 리스트
            mv.addObject("salesLists", new ArrayList<>());
        }
        return mv;
    }

    // 새로고침 시 중복 등록 방지 및 이동 시 URL을 맞춰주기 위해 forward 말고 redirect 사용했음!
    @PostMapping("/create")
    public String createSales(@Valid SalesDTO salesDTO, RedirectAttributes rdtat) {
        SalesDTO savedDTO = salesServiceImpl.createSales(salesDTO);

        if (savedDTO != null) {
            rdtat.addFlashAttribute("salesDTO", savedDTO);
            rdtat.addFlashAttribute("message", "수주서가 등록되었습니다.");
            return "redirect:/sales" + salesDTO.getSalesId();
        } else {
            rdtat.addFlashAttribute("message","수주서 등록에 실패하였습니다. 다시 시도해주세요.");
            return "redirect:/sales";
        }
    }
}
