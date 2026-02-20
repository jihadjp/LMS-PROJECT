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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    // Spring Security 6 এ session এ context save করার জন্য
    private final HttpSessionSecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

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

                    // ✅ নতুন SecurityContext তৈরি করে authentication set করো
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);

                    // ✅ HttpSessionSecurityContextRepository দিয়ে session এ save করো
                    // এটাই আসল fix — Spring Security 6 এ এটা না করলে পরের request এ context হারিয়ে যায়
                    securityContextRepository.saveContext(context, request, response);

                    log.info("Token auth successful, session saved for: {}", email);
                }
            } catch (Exception e) {
                log.error("Token auth error: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}