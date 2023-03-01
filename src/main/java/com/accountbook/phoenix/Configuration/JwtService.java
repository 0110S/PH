package com.accountbook.phoenix.Configuration;


import com.accountbook.phoenix.Exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    public String extractUserName(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> getClaim) {
        Claims claims = extractAllClaims(jwt);
        return getClaim.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSignInKey() {
        String secretKey = "2D4A404E635266556A586E3272357538782F413F4428472B4B6250645367566B";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) throws Exception {
        try {
            String userName = extractUserName(jwt);
            if (!userName.equals(userDetails.getUsername()) && isTokenExpired(jwt)) {
                throw new Exception("user not found");
            }
            return true;
        } catch (Exception e) {
            e.getMessage();
            e.getCause();
            return false;
        }
    }

    private boolean isTokenExpired(String jwt) {
        try {
            if (extractExpiration(jwt).before(new Date())) {
                throw new TokenExpiredException("token expired");
            }
        } catch (TokenExpiredException exception) {
            return false;
        }
        return false;
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }


    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .setExpiration(new Date(System.currentTimeMillis() + 1000000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
