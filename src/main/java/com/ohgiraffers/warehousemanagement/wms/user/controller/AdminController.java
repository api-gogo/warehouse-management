package com.ohgiraffers.warehousemanagement.wms.user.controller;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        String message = null;
        List<UserDTO> userDTOList = userService.getAllUsers();

        if (userDTOList.isEmpty()) {
            message = "가입된 회원이 없습니다.";
        }

        model.addAttribute("users", userDTOList);
        model.addAttribute("message", message);
        return "/admin/users";
    }

    @GetMapping("/users/{userId}")
    public String getUser(@PathVariable Integer userId, Model model) {
        String message = null;
        UserDTO userDTO = userService.getUserByUserId(userId);

        if (userDTO == null) {
            message = "해당 id의 유저 정보가 없습니다.";
        }
        model.addAttribute("user", userDTO);
        model.addAttribute("message", message);
        return "/admin/user-detail";
    }
}
