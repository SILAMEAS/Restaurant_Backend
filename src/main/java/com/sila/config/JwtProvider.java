package com.sila.config;

import com.sila.utlis.contants.JwtConstant;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {
    private static final Dotenv dotenv = Dotenv.load(); // Load from .env
    private static final long ACCESS_TOKEN_EXPIRATION = Long.parseLong(dotenv.get("ACCESS_TOKEN_EXPIRATION"));
    private static final long REFRESH_TOKEN_EXPIRATION = Long.parseLong(dotenv.get("REFRESH_TOKEN_EXPIRATION"));
    private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());


    // Existing method
    public String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);
        return Jwts.builder()
                .setIssuer(String.valueOf(new Date()))
                .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRATION)) // 1 day
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
    }

    // New method for generating refresh token
    public String generateRefreshToken(Authentication auth) {
        return Jwts.builder()
                .setIssuer(String.valueOf(new Date()))
                .setExpiration(new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRATION)) // 7 days
                .claim("email", auth.getName())
                .signWith(key)
                .compact();
    }

    // Existing method
    public String getEmailFromJwtToken(String jwt) {
        // If your tokens are prefixed with "Bearer ", remove it
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return String.valueOf(claims.get("email"));
    }


    // Existing method
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    // Validate the refresh token (optional)
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
