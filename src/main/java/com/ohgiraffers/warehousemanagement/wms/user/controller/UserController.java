package com.ohgiraffers.warehousemanagement.wms.user.controller;

import com.ohgiraffers.warehousemanagement.wms.auth.model.AuthDetails;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.SignupUserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.model.dto.UserDTO;
import com.ohgiraffers.warehousemanagement.wms.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public void getSignUpForm() {}

    @PostMapping("/signup")
    public String signUpUser(@ModelAttribute SignupUserDTO signupUserDTO, Model model) {

        Integer result = userService.registerUser(signupUserDTO);
        String message = null;

        if (result == -1) {
            message = "중복된 사원번호가 존재합니다.";
            model.addAttribute("message", message);
            return "user/signup";
        } else if (result == -2) {
            message = "중복된 이메일이 존재합니다.";
            model.addAttribute("message", message);
            return "user/signup";
        } else if (result == -3) {
            message = "중복된 전화번호가 존재합니다.";
            model.addAttribute("message", message);
            return "user/signup";
        } else if (result == 0) {
            message = "서버에 오류가 발생하였습니다.";
            model.addAttribute("message", message);
            return "user/signup";
        } else {
            message = "회원가입이 완료되었습니다.";
            model.addAttribute("message", message);
            return "auth/login";
        }
    }

    @GetMapping("/profile")
    public String getProfile(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof AuthDetails) {
                AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
                UserDTO userDTO = userService.getUserByUserId(authDetails.getUserId());

                model.addAttribute("user", userDTO);
                return "user/profile";
            }
        }

        return "redirect:/";
    }

    @GetMapping("/password-verify")
    public String getVerifyForm() {
        return "user/password-verify";
    }

    @PostMapping("/password-verify")
    public String verifyPassword(Authentication authentication, @RequestParam String verifyPassword, Model model) {
        String message = null;

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof AuthDetails) {
                AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();

                String encodedPassword = authDetails.getPassword();

                if (!passwordEncoder.matches(verifyPassword, encodedPassword)) {
                    message = "비밀번호가 일치하지 않습니다.";
                    model.addAttribute("message", message);
                    return "user/password-verify";
                }

                UserDTO userDTO = userService.getUserByUserId(authDetails.getUserId());
                model.addAttribute("user", userDTO);
                return "user/update";
            }
        }

        return "redirect:/";
    }

    @PatchMapping("/profile")
    public String updateProfile(Authentication authentication, @ModelAttribute UserDTO updateUser,
                                RedirectAttributes redirectAttributes) {
        String message = null;

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof AuthDetails) {
                AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
                Integer userId = authDetails.getUserId();
                
                boolean result = userService.updateProfile(userId, updateUser);

                if (!result) {
                    message = "회원 정보를 찾을 수 없습니다.";
                    redirectAttributes.addFlashAttribute("message", message);
                    return "redirect:/user/update";
                }

                // 비밀번호가 수정된 경우
                if (updateUser.getUserPass() != null && !updateUser.getUserPass().trim().isEmpty()) {
                    // 현재 HTTP 요청 및 세션 가져오기
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                    HttpSession session = request.getSession(false);

                    // 세션 무효화
                    if (session != null) {
                        session.invalidate();
                    }

                    // 보안 컨텍스트 클리어
                    SecurityContextHolder.clearContext();

                    redirectAttributes.addFlashAttribute("message",
                            "비밀번호가 성공적으로 변경되었습니다. 새 비밀번호로 다시 로그인해주세요.");
                    return "redirect:/";
                }

                // 비밀번호 외 정보만 수정된 경우
                message = "회원 정보가 업데이트 되었습니다.";
                redirectAttributes.addFlashAttribute("message", message);
                return "redirect:/user/profile";
            }
        }

        return "redirect:/";
    }
}