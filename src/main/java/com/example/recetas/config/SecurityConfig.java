package com.example.recetas.config;

import com.example.recetas.util.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(@Lazy JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF temporalmente
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/", "/usuarios/login", "/error", "/registro", "/usuarios/registro",
                                "/inicio", "/buscar-recetas")
                        .permitAll() // Permitir acceso sin autenticación a estas rutas
                        .anyRequest().authenticated()) // Requerir autenticación para todas las demás rutas
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones, solo JWT
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Agregar filtro para configurar los encabezados de seguridad
        http.addFilterAfter(securityHeadersFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Define el filtro que añade los encabezados de seguridad
    @Bean
    public Filter securityHeadersFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;

                // Configuración de Content-Security-Policy (CSP)
                httpServletResponse.setHeader("Content-Security-Policy",
                        "default-src 'self'; " +
                                "script-src 'self' https://code.jquery.com https://cdn.jsdelivr.net https://maxcdn.bootstrapcdn.com; "
                                + // Agrega los CDNs para scripts
                                "style-src 'self' https://maxcdn.bootstrapcdn.com; " + // Agrega el CDN para estilos de
                                                                                       // Bootstrap
                                "img-src 'self' data:; " +
                                "font-src 'self' https://fonts.gstatic.com; " +
                                "connect-src 'self'; " +
                                "frame-ancestors 'none'; " +
                                "object-src 'none'; " +
                                "base-uri 'self'; " +
                                "form-action 'self';");

                // Configuración de X-Frame-Options para denegar iframes
                httpServletResponse.setHeader("X-Frame-Options", "DENY");

                // Configuración de X-XSS-Protection
                httpServletResponse.setHeader("X-XSS-Protection", "1; mode=block");

                // Configuración de X-Content-Type-Options
                httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");

                // Configuración de Strict-Transport-Security (HSTS)
                httpServletResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
