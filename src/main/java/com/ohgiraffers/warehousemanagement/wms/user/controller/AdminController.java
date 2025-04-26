package com.ohgiraffers.warehousemanagement.wms.user.controller;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<UserDTO> userDTOList = userService.getAllUsers();

        if (userDTOList.isEmpty()) {
            String message = null;
            message = "가입된 회원이 없습니다.";
            model.addAttribute("message", message);
        }

        model.addAttribute("users", userDTOList);
        return "/admin/users";
    }

    @GetMapping("/users/{userId}")
    public String getUser(@PathVariable Integer userId, Model model) {
        UserDTO userDTO = userService.getUserByUserId(userId);

        if (userDTO == null) {
            String message = null;
            message = "해당 id의 유저 정보가 없습니다.";
            model.addAttribute("message", message);
        }
        model.addAttribute("user", userDTO);
        return "/admin/user-detail";
    }

    @PostMapping("/users/update/{userId}")
    public String updateUser(@PathVariable Integer userId, UserDTO updateUserDTO,
                             RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = userService.updateUser(userId, updateUserDTO);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "회원 정보가 업데이트 되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users/" + userId;
    }
}
