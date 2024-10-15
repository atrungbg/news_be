package com.example.NewBackEnd.config;

import com.example.NewBackEnd.enums.ErrorCode;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.exception.ErrorResponse;
import com.example.NewBackEnd.service.IJWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    IJWTService jwtService;

    @Autowired
    UserDetailsService userDetailsService;
//private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUserName(jwt);
            if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    logger.info("User authorities: " + userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext);
                    logger.info("JWT Authentication successful. User: " + userDetails.getUsername());
                    logger.info("Authorities: " + userDetails.getAuthorities());
                }
            }
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            logger.error("JWT Authentication failed: {}", e.fillInStackTrace());
            handleAuthenticationException(response, e);
        } catch (Exception e) {
            logger.error("Unexpected error during authentication: {}", e.fillInStackTrace());
            handleAuthenticationException(response,
                    new BaseException(ErrorCode.ERROR_500.getCode(), "Internal server error", ErrorCode.ERROR_500.getMessage()));
        }
    }
    private void handleAuthenticationException(HttpServletResponse response, BaseException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(403, e.getMessage(), "Forbidden");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}

