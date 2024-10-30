package com.example.recetas.util;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JWTUtil jwtTokenUtil;

    @Autowired
    public JWTRequestFilter(JWTUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // String path = request.getServletPath();

        // // Excluir rutas públicas y de error del filtro JWT
        // if (path.equals("/usuarios/login") || path.equals("/usuarios/registro") ||
        // path.equals("/login")
        // || path.equals("/error")) {
        // chain.doFilter(request, response);
        // return;
        // }

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println(authorizationHeader);
        String username = null;
        String jwt = null;

        if (authorizationHeader == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtTokenUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                System.out.println(e);
                System.out.println("JWT Token has expired");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Unable to get JWT Token");
            }
        }

        if (jwt != null && authorizationHeader == null) {
            try {
                System.out.println("CONCHETUMARE");
                username = jwtTokenUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                System.out.println(e);
                System.out.println("JWT Token has expired");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Unable to get JWT Token");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtTokenUtil.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
