package com.example.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

@Service
public class JWTService {

  // Fixed secret key (base64 encoded) — keep it secret and safe!
  private final String secretKey = "MzJieXRlc2xvbmdzZWNyZXRrZXlmb3Jqd3QxMjM0NTY3ODkwMTIzNA=="; // 32 bytes
  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", username);

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
        .signWith(getKey())
        .compact();
  }

  public String extractUsernameFromToken(String token) {
    return extractAllClaims(token).getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
