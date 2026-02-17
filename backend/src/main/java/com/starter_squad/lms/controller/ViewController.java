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

    private final UserService userService;

    // ১. application.yml থেকে ফ্রন্টএন্ড ইউআরএল নিয়ে আসা
    @Value("${app.frontend-url}")
    private String frontendUrl;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            // সেশন না থাকলে রিঅ্যাক্ট লগইন পেজে পাঠিয়ে দিবে
            return "redirect:" + frontendUrl + "/login";
        }

        String role = principal.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_SUPER_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (role.equals("ROLE_INSTRUCTOR")) {
            return "redirect:/instructor/dashboard";
        } else {
            // স্টুডেন্টদের রিঅ্যাক্ট কোর্সেস পেজে পাঠিয়ে দিবে
            return "redirect:" + frontendUrl + "/courses";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        // থাইমলিফ ফাইল না খুঁজে সরাসরি রিঅ্যাক্ট ইউআরএল-এ পাঠিয়ে দিবে
        return "redirect:" + frontendUrl + "/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        // রিঅ্যাক্ট রেজিস্ট্রেশন পেজে পাঠিয়ে দিবে
        return "redirect:" + frontendUrl + "/register";
    }

    // access-denied এবং error মেথডগুলো যদি থাইমলিফ ফাইল থাকে তবেই রাখুন, নাহলে সেগুলোও রিডাইরেক্ট করুন।

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