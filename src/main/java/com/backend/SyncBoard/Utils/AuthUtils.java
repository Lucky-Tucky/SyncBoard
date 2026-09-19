package com.backend.SyncBoard.Utils;

import com.backend.SyncBoard.Model.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HexFormat;

@Component
public class AuthUtils {

    @Value("${spring.jwt.key}")
    private String jwtKey;

    @Value("${spring.bcrypt.strength}")
    private int strength;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(CustomUserDetail user,long expiryTime){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId",user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ (long) expiryTime *60*100))
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


    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing token", e);
        }
    }

}
