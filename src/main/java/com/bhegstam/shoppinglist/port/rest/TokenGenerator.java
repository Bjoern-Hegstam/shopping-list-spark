package com.bhegstam.shoppinglist.port.rest;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

public class TokenGenerator {
    public static String generate(String username, byte[] tokenSecret) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setSubject(username);
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setIssuedAtToNow();

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));
        return jws.getCompactSerialization();
    }
}
