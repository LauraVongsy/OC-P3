package com.chatop.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Component used to create, check and validate JWT tokens and confirm user identities in the application.
 */
@Component
public class JwtService {

    @Value("${custom.secret}")
    private String secret;

    /**
     * Generate JWT token for the given username
     *
     * @param userName Username (here email is used as userName)
     * @return A built JWT token
     */
    public String generateToken(String userName, Integer userId) {
        //Prepare claims for the token
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userName);
        claims.put("userId", userId);
        //Build JWT Token with claims, issued time, expiration time and signIn key
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)).signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Creates a signIn key encoded
     *
     * @return The signIn key
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from the JWT token
     *
     * @param token         The JWT token
     * @param claimResolver A function to extract the claim
     * @return The value of the specified claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
