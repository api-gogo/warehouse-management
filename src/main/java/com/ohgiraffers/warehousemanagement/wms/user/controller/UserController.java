package com.ohgiraffers.warehousemanagement.wms.user.controller;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.SignupUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public void signup() {}

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupUserDTO signupUserDTO, Model model) {

        Integer result = userService.registerUser(signupUserDTO);
        String message = null;
        String view = null;

        if (result == null) {
            message = "중복 회원이 존재합니다.";
            view = "user/signup";
        } else if (result == 0) {
            message = "서버에 오류가 발생하였습니다.";
            view = "user/signup";
        } else {
            message = "회원가입이 완료되었습니다.";
            view = "auth/login";
        }

        model.addAttribute("message", message);
        return view;
    }
}
