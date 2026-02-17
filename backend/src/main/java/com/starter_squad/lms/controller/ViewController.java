package com.starter_squad.lms.controller;

import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.security.UserPrincipal;
import com.starter_squad.lms.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ViewController {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserPrincipal principal) {
        String cleanUrl = (frontendUrl.endsWith("/")) ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;

        if (principal == null) {
            return "redirect:" + cleanUrl + "/login";
        }

        String role = principal.getAuthorities().iterator().next().getAuthority();
        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (role.equals("ROLE_INSTRUCTOR")) {
            return "redirect:/instructor/dashboard";
        }

        return "redirect:" + cleanUrl + "/courses";
    }

    // ব্যাকএন্ডে এই মেথডগুলোর দরকার নেই, সিকিউরিটি কনফিগারেশন এগুলো হ্যান্ডেল করবে
    @GetMapping("/login")
    public String loginPage() {
        String cleanUrl = (frontendUrl.endsWith("/")) ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;
        return "redirect:" + cleanUrl + "/login";
    }

    // ==========================================
    // ERROR HANDLERS
    // ==========================================

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }

    @GetMapping("/error")
    public String error() {
        return "error/404";
    }
}