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
    @ResponseBody  // ← এই annotation যোগ করুন
    public ResponseEntity<String> home(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.ok("LMS Backend is running");  // Render এর health check pass হবে
        }

        String role = principal.getAuthorities().iterator().next().getAuthority();
        String cleanUrl = (frontendUrl.endsWith("/")) ? frontendUrl.substring(0, frontendUrl.length() - 1) : frontendUrl;

        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_INSTRUCTOR")) {
            return ResponseEntity.ok("redirect:/admin/dashboard");
        }
        return ResponseEntity.ok("redirect:" + cleanUrl + "/courses");
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