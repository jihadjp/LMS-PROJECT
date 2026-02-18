package com.starter_squad.lms.security.jwt;

import com.starter_squad.lms.security.util.JwtUtils;
import com.starter_squad.lms.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        if (token != null && !token.isEmpty()) {
            try {
                if (jwtUtils.validateJwtToken(token)) {
                    String email = jwtUtils.getEmailFromJwtToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    request.getSession().setAttribute(
                            "SPRING_SECURITY_CONTEXT",
                            SecurityContextHolder.getContext()
                    );

                    log.info("Token auth successful for user: {}", email);
                }
            } catch (Exception e) {
                log.error("Token auth error: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}