package com.backend.SyncBoard.Utils;

import com.backend.SyncBoard.Model.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class AuthUtils {

    @Value("${spring.jwt.key}")
    private String jwtKey;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(CustomUserDetail user){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId",user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*100))
                .signWith(getSigningKey())
                .compact();
    }
    public String getJwtClaim(String token){

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();

    }

}
