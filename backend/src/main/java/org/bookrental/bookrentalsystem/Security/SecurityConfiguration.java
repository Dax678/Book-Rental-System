package org.bookrental.bookrentalsystem.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**",
            "/api/books", "/api/books/{id}", "/api/promotions"
    };

    private static final String[] USER_ENDPOINTS = {
            //TODO: "/api/transactions/return/{rentalId}"
            "/api/transactions/rent", "/api/transactions/return/{rentalId}", "/api/users/me",
            "/api/transactions/user/me"
    };

    private static final String[] LIBRARIAN_ENDPOINTS = {
            "/api/users/{id}", "/api/users", "/api/users/name/{name}",
            "/api/transactions", "/api/transactions/book/{bookId}", "/api/books/save",
            "/api/promotions/save", "/api/prices/history", "/api/transactions/user/{userId}"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(USER_ENDPOINTS).hasAnyRole("ADMIN", "LIBRARIAN", "STUDENT", "CUSTOMER")
                        .requestMatchers(LIBRARIAN_ENDPOINTS).hasAnyRole("ADMIN", "LIBRARIAN")
                        .anyRequest().authenticated())
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
