package com.ohgiraffers.warehousemanagement.wms.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("login")
    public void login() {}

    @GetMapping("/fail")
    public String loginFail(@RequestParam(value="message", required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "auth/fail";
    }
}
