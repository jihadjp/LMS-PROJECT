package com.starter_squad.lms.controller;

import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.security.UserPrincipal;
import com.starter_squad.lms.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ViewController {

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    // ==========================================
    // HOME/ROOT ROUTE
    // ==========================================

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String role = principal.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_SUPER_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (role.equals("ROLE_INSTRUCTOR")) {
            return "redirect:/instructor/dashboard";
        } else if (role.equals("ROLE_USER") || role.equals("ROLE_STUDENT")) {
            return "redirect:/dashboard";
        }

        return "redirect:/login";
    }

    // ==========================================
    // AUTHENTICATION ROUTES
    // ==========================================

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String success,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (success != null) {
            model.addAttribute("success", "Registration successful! Please login.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute User userDto, Model model) {
        try {
            userService.createUser(userDto);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
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