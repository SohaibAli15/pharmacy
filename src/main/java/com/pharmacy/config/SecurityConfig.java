package com.pharmacy.config;

import com.pharmacy.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // TEMPORARY: Allow all requests without authentication
                .anyRequest().permitAll()
            );

        return http.build();
    }

    // PRODUCTION CONFIGURATION (Commented for now - uncomment when ready to add authentication)
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(authz -> authz
//                // Allow public access to Swagger UI and API documentation
//                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
//                .requestMatchers("/v3/api-docs/**", "/api-docs/**").permitAll()
//                .requestMatchers("/swagger-resources/**", "/webjars/**").permitAll()
//                // Allow public access to actuator health endpoint
//                .requestMatchers("/actuator/health/**").permitAll()
//                // API endpoints - require authentication and specific roles
//                .requestMatchers("/api/users/**").hasRole("ADMIN")
//                .requestMatchers("/api/medicines/**").hasAnyRole("ADMIN", "PHARMACIST")
//                .requestMatchers("/api/prescriptions/**").hasAnyRole("ADMIN", "PHARMACIST")
//                .requestMatchers("/api/sales/**").hasAnyRole("ADMIN", "PHARMACIST")
//                .requestMatchers("/api/**").hasAnyRole("ADMIN", "PHARMACIST", "USER")
//                // All other requests require authentication
//                .anyRequest().authenticated()
//            )
//            .httpBasic(httpBasic -> {})
//            .authenticationProvider(authenticationProvider());
//
//        return http.build();
//    }
}
