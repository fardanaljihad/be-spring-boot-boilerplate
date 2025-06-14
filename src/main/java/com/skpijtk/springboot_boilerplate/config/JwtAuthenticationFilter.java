package com.skpijtk.springboot_boilerplate.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skpijtk.springboot_boilerplate.service.JwtService;

import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        
        String path = request.getServletPath();
        if (
            path.equals("/api/v1/admin/signup") || 
            path.equals("/api/v1/admin/login") ||
            path.equals("/api/v1/mahasiswa/login") ||
            path.equals("/ws/**") ||
            path.equals("/actuator/**")
        ) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (SignatureException ex) {
            sendErrorResponse(
                response, 
                request, 
                HttpStatus.UNAUTHORIZED, 
                "AUTH-ERR-401", 
                "Token authorization pada Collection dan Headers tidak sesuai. Cek kembali token di Postman, jika Anda menggunakan Postman."
            );
            return;
        }
    }

    private void sendErrorResponse(
        HttpServletResponse response,
        HttpServletRequest request,
        HttpStatus status,
        String errorCode,
        String message
    ) throws IOException {

    response.setStatus(status.value());
    response.setContentType("application/json");

    String json = String.format(
        "{ \"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"errorCode\": \"%s\", \"path\": \"%s\" }",
        LocalDateTime.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        errorCode,
        request.getRequestURI()
    );

    response.getWriter().write(json);
}
}
