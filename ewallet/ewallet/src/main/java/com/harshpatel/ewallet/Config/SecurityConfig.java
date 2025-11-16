package com.harshpatel.ewallet.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
                .authorizeHttpRequests(auth -> auth
                        // Permit all for payment & QR endpoints
                        .requestMatchers("/api/payment/webhook").permitAll()
                        .requestMatchers("/api/qr/**").permitAll()
                        // Everything else can be authenticated or permitAll depending on your login flow
                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // Disable default basic auth
                .formLogin(form -> form.disable());          // Disable form login

        return http.build();
    }
}
