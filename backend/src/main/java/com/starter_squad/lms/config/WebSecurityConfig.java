package com.starter_squad.lms.config;

import com.starter_squad.lms.security.jwt.JwtAuthTokenFilter;
import com.starter_squad.lms.security.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthTokenFilter jwtAuthTokenFilter;

    // ১. প্রফেশনাল রিডাইরেক্টের জন্য ফ্রন্টএন্ড ইউআরএল ইনজেক্ট করা
    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ========================================================================
    // ১. API Security Chain: React Front-end এর জন্য (JWT Based)
    // ========================================================================
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // অত্যন্ত গুরুত্বপূর্ণ: STATELESS থেকে IF_REQUIRED এ পরিবর্তন করা হয়েছে
                // এর ফলে এটি একইসাথে JWT এবং ব্রাউজার সেশন (Cookies) চিনতে পারবে
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(eh -> eh.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/*/profile-image").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()

                        // রোল ভিত্তিক পারমিশন
                        .requestMatchers(HttpMethod.POST, "/api/courses", "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasRole("ADMIN")

                        .requestMatchers("/api/assessments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/enrollments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/feedbacks/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/learning/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/progress/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/questions/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                // JWT ফিল্টারটি সবশেষে চেক করবে (React অ্যাপের জন্য)
                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ========================================================================
    // ২. Web Security Chain: Thymeleaf Admin/Instructor প্যানেলের জন্য (Session Based)
    // ========================================================================
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/instructor/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                        .anyRequest().permitAll()
                )
                // ============================================================
                // ১. সেশন এক্সপায়ার হলে ৩০০০ পোর্টে রিডাইরেক্ট করার লজিক
                // ============================================================
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            // সেশন না থাকলে ইউজারকে সরাসরি রিঅ্যাক্ট লগইন পেজে পাঠিয়ে দিবে
                            response.sendRedirect(frontendUrl + "/login?session_expired=true");
                        })
                )
                // ============================================================
                .formLogin(form -> form
                        .loginPage("/login") // এটি ব্যাকআপ হিসেবে থাকবে
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(frontendUrl + "/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        // ২. সেশন টাইমআউট হলে কোথায় যাবে সেটিও বলে দেওয়া যায়
                        .invalidSessionUrl(frontendUrl + "/login?timeout=true")
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // ৫. সেশন ব্রিজ ব্যবহারের জন্য এটি জরুরি
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}