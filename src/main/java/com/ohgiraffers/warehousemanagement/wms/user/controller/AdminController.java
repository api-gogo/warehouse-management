package com.ohgiraffers.warehousemanagement.wms.user.controller;

import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.AdminService;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(AdminService adminService, UserServiceImpl userServiceImpl) {
        this.adminService = adminService;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/users")
    public String listUsers(@RequestParam(required = false) String search,
                           @RequestParam(required = false, defaultValue = "all") String statusTab,
                           @RequestParam(required = false, defaultValue = "all") String roleTab,
                           @RequestParam(required = false, defaultValue = "all") String partTab,
                           @PageableDefault(size = 10, sort = "userCreatedAt", direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {

        // 검색어가 빈 문자열일 경우 null로 처리
        if (search != null && search.trim().isEmpty()) {
            search = null;
        }

        Page<UserDTO> userPage = adminService.getUsersByPartRoleAndStatus(search, partTab, roleTab, statusTab, pageable);

        if (userPage.isEmpty()) {
            model.addAttribute("message", "조건에 맞는 회원이 없습니다.");
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("size", userPage.getSize());

        // 승인 대기 회원 수 조회
        long pendingCount = adminService.countPendingUsers();
        model.addAttribute("pendingCount", pendingCount);

        model.addAttribute("search", search);

        return "admin/users/list";
    }

    @GetMapping("/users/{userId}")
    public String showUserDetail(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
        UserDTO userDTO = userServiceImpl.findById(userId);

        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("message", "해당 id의 유저 정보가 없습니다.");
            return "redirect:/admin/users";
        }
        
        model.addAttribute("user", userDTO);
        return "admin/users/detail";
    }

    @GetMapping("/users/{userId}/edit")
    public String showUserEditForm(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
        UserDTO userDTO = userServiceImpl.findById(userId);

        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("message", "해당 id의 유저 정보가 없습니다.");
            return "redirect:/admin/users";
        }
        
        model.addAttribute("user", userDTO);
        return "admin/users/edit";
    }

    @PatchMapping("/users/{userId}")
    public String updateUser(@PathVariable Long userId, UserDTO updateUserDTO,
                             RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.updateUser(userId, updateUserDTO);

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
    public String approveUser(@PathVariable Long userId, UserDTO updateUserDTO,
                             RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.approveUser(userId, updateUserDTO);

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
    public String rejectUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.rejectUser(userId);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "승인이 거부되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/disable")
    public String disableUser(@PathVariable Long userId,
                              RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.disableUser(userId);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "회원이 휴직 처리되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/enable")
    public String enableUser(@PathVariable Long userId,
                              RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.enableUser(userId);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "회원이 복직 처리되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/resign")
    public String resignUser(@PathVariable Long userId,
                             RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.resignUser(userId);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "회원이 퇴사 처리되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/blacklist")
    public String blacklistUser(@PathVariable Long userId,
                               RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.blacklistUser(userId);

        if (!result) {
            message = "회원 정보를 찾을 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "회원이 영구 차단되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/reset-to-pending")
    public String resetToPending(@PathVariable Long userId,
                                RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.resetToApprovalPending(userId);

        if (!result) {
            message = "회원 정보를 찾을 수 없거나 승인거부 상태가 아닙니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users";
        }

        message = "회원 상태가 승인대기로 변경되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/approvals")
    public String listPendingUsers(@RequestParam(required = false) String search,
                                  @PageableDefault(size = 10) Pageable pageable,
                                  Model model) {
        // 검색어가 빈 문자열일 경우 null로 처리
        if (search != null && search.trim().isEmpty()) {
            search = null;
        }

        Page<UserDTO> userPage = adminService.getPendingUsers(search, pageable);

        if (userPage.isEmpty()) {
            model.addAttribute("message", "승인 대기 중인 회원이 없습니다.");
        }

        model.addAttribute("pendingUsers", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("size", userPage.getSize());

        // 승인 대기 회원 수 조회
        long pendingCount = adminService.countPendingUsers();
        model.addAttribute("pendingCount", pendingCount);

        model.addAttribute("search", search);

        return "admin/users/approvals";
    }

    @PostMapping("/users/approve-batch")
    public String approveBatchUsers(@RequestParam("selectedUsers") List<Long> userIds,
                                    @RequestParam("batchPart") String batchPart,
                                    @RequestParam("batchRole") String batchRole,
                                    RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.approveUsers(userIds, batchPart, batchRole);

        if (!result) {
            message = "승인 도중 오류가 발생하였습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users/approvals";
        }

        message = "선택한 사용자들이 성공적으로 승인되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users/approvals";
    }

    @PostMapping("/users/reject-batch")
    public String rejectBatchUsers(@RequestParam("selectedUsers") List<Long> userIds,
                                    RedirectAttributes redirectAttributes) {
        String message = null;
        boolean result = adminService.rejectUsers(userIds);

        if (!result) {
            message = "승인 거부 도중 오류가 발생하였습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/users/approvals";
        }

        message = "선택한 사용자들이 성공적으로 승인 거부되었습니다.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/users/approvals";
    }
}