package com.social.app.security.jwt;

import com.social.app.model.User;
import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${JWT_LOGIN_SECRET}")
    private String jwtLoginSecret;
    @Value("${JWT_FORGET_SECRET}")
    private String jwtForgetSecret;
    @Value("${JWT_VERIFY_SECRET}")
    private String jwtVerifySecret;
    @Value("${JWT_LOGIN_EXPIRATION_IN_MS}")
    private int jwtLoginExpirationMs;
    @Value("${JWT_FORGET_EXPIRATION_IN_MS}")
    private int jwtForgetExpirationMs;
    @Value("${JWT_VERIFY_EXPIRATION_IN_MS}")
    private int jwtVerifyExpirationMs;

    public String generateLoginToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        return generateToken(userPrincipal.getUsername(),jwtLoginSecret, jwtLoginExpirationMs);
    }

    public String generateVerifyToken(String userId) {
//        System.out.println(jwtVerifySecret+" "+jwtVerifyExpirationMs);
        return generateToken(userId,jwtVerifySecret, jwtVerifyExpirationMs);
    }

    public String generateForgetPasswordToken(String userId){
        return generateToken(userId,jwtForgetSecret,jwtForgetExpirationMs);
    }


    private String generateToken(String subject, String secretKey, int expirationTimeInMs){
        return Jwts.builder()
                   .setSubject(subject)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMs))
                   .signWith(getSignInKey(secretKey),SignatureAlgorithm.HS256)
                   .compact();
    }



    private Key getSignInKey(String jwtSecret){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserNameFromJwtToken(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtLoginSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
