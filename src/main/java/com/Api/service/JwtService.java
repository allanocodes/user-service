package com.Api.service;

import com.Api.Exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private String secretKey = "";

    private void generateKeyIfNeeded() {
        if (secretKey == null || secretKey.isEmpty()) {
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        }
    }

    public SecretKey getKey(){
        generateKeyIfNeeded();
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }


    public String generateToken(String username){

            String token = Jwts.builder()
                    .claim("role", "USER")
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                    .signWith(getKey()).compact();
            return token;

    }

    public Claims getClaims(String token){
        try {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token)
                .getBody();


        return claims;

        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException |
                SignatureException e)
        {
            throw new InvalidTokenException("Invalid Token",e);
        }

    }

    public String extractUsername(String token){
        Claims claims = getClaims(token);

        String username = claims.getSubject();
        return username;
    }

    public boolean validateToken(String token){

        return isTokenExpired(token);


    }

    public boolean isTokenExpired(String token){
        Claims claims = getClaims(token);
        Date date = claims.getExpiration();

        return  date.after(new Date());

    }

}
