package com.ohgiraffers.warehousemanagement.wms.returning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/returns")
public class ReturningHomeController {

    @GetMapping
    public String returningHome() {
        return "returns/Home";
    }
}
