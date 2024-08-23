package com.wallet.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${token.signing.key}")
    public String secret;

    public String validateTokenAndGetClaim(String token) throws JWTVerificationException {
        token = token.substring(7);
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("RBNK")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("login").asString();
    }
}
