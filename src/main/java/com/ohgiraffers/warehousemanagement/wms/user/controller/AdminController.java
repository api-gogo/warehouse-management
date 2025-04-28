package com.ohgiraffers.warehousemanagement.wms.user.controller;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String getUsers(@RequestParam(required = false) String search,
                           @RequestParam(required = false, defaultValue = "all") String status,
                           @PageableDefault(size = 10) Pageable pageable,
                           Model model) {

        // 검색어가 빈 문자열일 경우 null로 처리
        if (search != null && search.trim().isEmpty()) {
            search = null;
        }

        Page<UserDTO> userPage = userService.findUsers(search, status, pageable);

        if (userPage.isEmpty()) {
            model.addAttribute("message", "조건에 맞는 회원이 없습니다.");
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("size", userPage.getSize());

        // 승인 대기 회원 수 조회
        long pendingCount = userService.countPendingUsers();
        model.addAttribute("pendingCount", pendingCount);

        model.addAttribute("search", search);
        model.addAttribute("status", status);

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

    @PatchMapping("/users/{userId}")
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

    @PostMapping("/users/{userId}/approve")
    public String approveUser(@PathVariable Integer userId, UserDTO updateUserDTO,
                             RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = userService.approveUser(userId, updateUserDTO);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "회원이 승인되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users/" + userId;
    }

    @PostMapping("/users/{userId}/reject")
    public String rejectUser(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = userService.rejectUser(userId);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "승인이 거부되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users/" + userId;
    }
}
