package com.example.demo.config;

import com.example.demo.security.CustomAccessDeniedHandler;
import com.example.demo.security.CustomAuthEntryPoint;
import com.example.demo.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomAuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // disable CSRF because I use JWT (stateless API)
                .csrf(csrf -> csrf.disable())
                // enable CORS so React frontend can call backend
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // public + private API rules
                .authorizeHttpRequests(auth -> auth
                        // allow login + register without token
                        .requestMatchers("/auth/**").permitAll()
                        // all other APIs need login
                        .anyRequest().authenticated()
                )
                // custom error handling
                .exceptionHandling(ex -> ex
                        // if user not logged in or token wrong
                        .authenticationEntryPoint(authEntryPoint)

                        // if user logged in but no permission
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // JWT filter runs before username/password filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // allow to react frontend
        config.setAllowedOrigins(List.of(frontendUrl));

        // allow all methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // allow headers
        config.setAllowedHeaders(List.of("*"));

        // allow token header
        config.setExposedHeaders(List.of("Authorization"));

        // allow cookies / auth headers
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // encrypt password using bcrypt
        return new BCryptPasswordEncoder();
    }
}
