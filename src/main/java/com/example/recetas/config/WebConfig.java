package com.example.recetas.config;

import com.example.recetas.util.JWTInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JWTInterceptor jwtTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**") // Aplicar a todas las rutas
                .excludePathPatterns("/login", "/registro", "/usuarios/login", "/usuarios/registro"); // Excluir rutas
                                                                                                      // públicas
    }
}
