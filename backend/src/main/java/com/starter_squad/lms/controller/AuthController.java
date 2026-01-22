package com.starter_squad.lms.controller;

import com.starter_squad.lms.dto.ApiResponse;
import com.starter_squad.lms.dto.JwtResponseDTO;
import com.starter_squad.lms.dto.LoginRequestDTO;
import com.starter_squad.lms.entity.User;
import com.starter_squad.lms.security.UserPrincipal;
import com.starter_squad.lms.security.util.JwtUtils;
import com.starter_squad.lms.service.UserService;
import jakarta.servlet.http.HttpServletRequest; // যুক্ত করুন
import jakarta.servlet.http.HttpSession;     // যুক্ত করুন
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequest,
            HttpServletRequest request) { // HttpServletRequest যুক্ত করা হয়েছে

        log.info("Login attempt for email: {}", loginRequest.getEmail());

        // ১. ইউজার অথেন্টিকেশন
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // ২. সিকিউরিটি কনটেক্সট আপডেট
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String role = userPrincipal.getAuthorities().iterator().next().getAuthority();

        // ============================================================
        // ৩. SESSION BRIDGE (Admin/Instructor এর জন্য সেশন তৈরি)
        // ============================================================
        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_INSTRUCTOR")) {
            HttpSession session = request.getSession(true); // নতুন সেশন তৈরি
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            log.info("Session bridge created for role: {} | Email: {}", role, loginRequest.getEmail());
        }
        // ============================================================

        // ৪. JWT টোকেন তৈরি (React Frontend এর জন্য)
        String jwt = jwtUtils.generateJwtToken(authentication);

        JwtResponseDTO jwtResponse = JwtResponseDTO.builder()
                .token(jwt)
                .type("Bearer")
                .id(userPrincipal.getId())
                .email(userPrincipal.getEmail())
                .name(userPrincipal.getName())
                .role(role)
                .build();

        log.info("User logged in successfully: {}", loginRequest.getEmail());
        return ResponseEntity.ok(new ApiResponse<>("Login successful", jwtResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody User signUpRequest) {
        log.info("Registration attempt for email: {}", signUpRequest.getEmail());
        User user = authService.createUser(signUpRequest);
        log.info("User registered successfully: {}", signUpRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("User registered successfully", user));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        // সেশন ইনভ্যালিডেট করা
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse<>("Logout successful", null));
    }
}