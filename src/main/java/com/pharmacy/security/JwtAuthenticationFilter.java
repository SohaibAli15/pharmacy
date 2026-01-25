/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired private JwtUtil jwtUtil;

  @Autowired private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    String username = null;
    String jwt = null;

    logger.info("Authorization header: {}", authHeader);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
      logger.info("Extracted JWT: {}", jwt);
      try {
        username = jwtUtil.extractUsername(jwt);
        logger.info("Extracted username from JWT: {}", username);
      } catch (Exception e) {
        logger.error("Failed to extract username from JWT: {}", e.getMessage());
      }
    } else {
      logger.warn("No Bearer token found in Authorization header");
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      boolean valid = false;
      try {
        valid = jwtUtil.validateToken(jwt, userDetails.getUsername());
        logger.info("JWT validation result: {}", valid);
      } catch (Exception e) {
        logger.error("JWT validation error: {}", e.getMessage());
      }
      if (valid) {
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info("Authentication set in SecurityContext for user: {}", username);
      } else {
        logger.warn("JWT is invalid or expired for user: {}", username);
      }
    } else if (username != null) {
      logger.info("Authentication already present in SecurityContext for user: {}", username);
    }
    filterChain.doFilter(request, response);
  }
}
