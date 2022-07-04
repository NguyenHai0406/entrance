package com.test.entrance.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtToken {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("isUser", true);
        return doGenerateToken(claims, userName);
    }

    public String generateRefreshToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("isUser", true);
        return doGenerateRefreshToken(claims, userName);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        Date newDate = new Date();
        newDate.setMonth(newDate.getMonth() + 1);
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(newDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }
}
