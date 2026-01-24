/* Copyright (C) Pharmacy Management System - All Rights Reserved */
package com.pharmacy.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secret;

  private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

  /**
   * The secret key for HS256 must be at least 256 bits (32+ ASCII chars). Example:
   * my-very-strong-secret-key-123456 If not, JWT will throw WeakKeyException.
   */
  private SecretKey getSigningKey() {
    if (secret == null || secret.length() < 32) {
      throw new IllegalArgumentException(
          "JWT secret key must be at least 32 characters for HS256 security. Current length: "
              + (secret == null ? 0 : secret.length()));
    }
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }
}
