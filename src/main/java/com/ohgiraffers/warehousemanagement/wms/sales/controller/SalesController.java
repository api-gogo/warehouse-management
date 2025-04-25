package com.ohgiraffers.warehousemanagement.wms.sales.controller;

import com.ohgiraffers.warehousemanagement.wms.sales.model.dto.SalesDTO;
import com.ohgiraffers.warehousemanagement.wms.sales.service.SalesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sales")
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
}
