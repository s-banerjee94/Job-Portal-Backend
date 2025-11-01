package com.example.jpb.config;

import com.example.jpb.security.CustomUserDetailsService;
import com.example.jpb.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - Swagger/OpenAPI Documentation
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Public endpoints - Authentication
                        .requestMatchers("/api/auth/register/candidate").permitAll()
                        .requestMatchers("/api/auth/register/recruiter").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()

                        // Public endpoints - Job viewing (GET only)
                        .requestMatchers(HttpMethod.GET, "/api/jobs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jobs/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jobs/search").permitAll()

                        // Recruiter-only endpoints
                        .requestMatchers(HttpMethod.POST, "/api/jobs").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasRole("RECRUITER")
                        .requestMatchers("/api/jobs/mine").hasRole("RECRUITER")
                        .requestMatchers("/api/applications/**").hasRole("RECRUITER")

                        // Candidate-only endpoints
                        .requestMatchers("/api/candidates/**").hasRole("CANDIDATE")
                        .requestMatchers(HttpMethod.POST, "/api/jobs/{id}/apply").hasRole("CANDIDATE")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
