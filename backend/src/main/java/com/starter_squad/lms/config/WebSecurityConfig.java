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

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(eh -> eh.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/*/profile-image").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()
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
                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // এখানেও CORS যুক্ত করা হলো
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/instructor/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                        .anyRequest().permitAll()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            // সেশন না থাকলে রিডাইরেক্ট
                            response.sendRedirect(frontendUrl + "/login?session_expired=true");
                        })
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(frontendUrl + "/login?logout=true") // ডাইনামিক ইউআরএল
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl(frontendUrl + "/login?timeout=true") // ডাইনামিক ইউআরএল
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ১. লোকাল এবং প্রোডাকশন উভয় ইউআরএল-ই লিস্টে রাখা হলো
        // Render-এর Environment Variable থেকে frontendUrl আসবে
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000", frontendUrl));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}