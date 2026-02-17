package com.starter_squad.lms.controller;

import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.security.UserPrincipal;
import com.starter_squad.lms.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
            // Render health check HEAD request এর জন্য কাজ করবে,
            // browser GET এর জন্য frontend এ পাঠাবে
            return "redirect:" + cleanUrl;  // /login নয়, শুধু frontend root এ পাঠাও
        }

        String role = principal.getAuthorities().iterator().next().getAuthority();
        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (role.equals("ROLE_INSTRUCTOR")) {
            return "redirect:/instructor/dashboard";
        }

        return "redirect:" + cleanUrl + "/courses";
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