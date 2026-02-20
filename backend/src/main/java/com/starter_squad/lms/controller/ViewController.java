package com.starter_squad.lms.controller;

import com.starter_squad.lms.security.UserPrincipal;
import com.starter_squad.lms.security.util.JwtUtils;
import com.starter_squad.lms.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ViewController {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    private String getCleanUrl() {
        return frontendUrl.endsWith("/")
                ? frontendUrl.substring(0, frontendUrl.length() - 1)
                : frontendUrl;
    }

    @GetMapping("/")
    public String home(@org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return "redirect:" + getCleanUrl();
        }
        String role = principal.getAuthorities().iterator().next().getAuthority();
        if (role.equals("ROLE_ADMIN")) return "redirect:/admin/dashboard";
        if (role.equals("ROLE_INSTRUCTOR")) return "redirect:/instructor/dashboard";
        return "redirect:" + getCleanUrl() + "/courses";
    }

    /**
     * ✅ এই endpoint টাই আসল fix।
     * Frontend থেকে সরাসরি browser navigation দিয়ে আসে (AJAX নয়)।
     * Token validate করে session তৈরি করে dashboard এ redirect করে।
     * Browser এই request থেকে session cookie পায় এবং পরের request এ পাঠায়।
     */
    @GetMapping("/auth/redirect")
    public String authRedirect(
            @RequestParam String token,
            @RequestParam String role,
            HttpServletRequest request) {

        try {
            if (!jwtUtils.validateJwtToken(token)) {
                return "redirect:" + getCleanUrl() + "/login?session_expired=true";
            }

            String email = jwtUtils.getEmailFromJwtToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

            // ✅ SecurityContext তৈরি করে session এ save করো
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // ✅ এটাই key — browser এই response এর cookie পাবে এবং save করবে
            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );

            // Role অনুযায়ী dashboard এ redirect
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (role.equals("ROLE_INSTRUCTOR")) {
                return "redirect:/instructor/dashboard";
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        return "redirect:" + getCleanUrl() + "/login?session_expired=true";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }

    @GetMapping("/error")
    public String error() {
        return "error/404";
    }
}