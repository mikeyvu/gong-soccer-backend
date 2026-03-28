package com.mikey.gongsoccer.service;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    public String extractUsername(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver);
    public String generateToken(UserDetails userDetails);
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    public long getExpirationTime();
    public boolean isTokenValid(String token, UserDetails userDetails);
}
