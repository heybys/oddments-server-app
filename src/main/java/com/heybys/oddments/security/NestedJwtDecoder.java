package com.heybys.oddments.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class NestedJwtDecoder implements JwtDecoder {
    private final RSAPrivateKey decryptionKey;
    private final RSAPublicKey verificationKey;
    private OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();

    public NestedJwtDecoder(RSAPrivateKey decryptionKey, RSAPublicKey verificationKey) {
        this.decryptionKey = decryptionKey;
        this.verificationKey = verificationKey;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // 1. Parse encrypted JWT
            JWEObject jweObject = JWEObject.parse(token);

            // 2. Decrypt
            jweObject.decrypt(new RSADecrypter(decryptionKey));

            // 3. Extract signed JWT from payload
            SignedJWT signedJWT = jweObject.getPayload().toSignedJWT();

            // 4. Verify signature
            if (!signedJWT.verify(new RSASSAVerifier(verificationKey))) {
                throw new JwtException("JWT signature verification failed");
            }

            // 5. Extract claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // 6. 만료 시간 체크
            Date expirationTime = claims.getExpirationTime();
            if (expirationTime != null && new Date().after(expirationTime)) {
                throw new JwtException("JWT is expired");
            }

            return new Jwt(
                    token,
                    claims.getIssueTime().toInstant(),
                    claims.getExpirationTime().toInstant(),
                    Map.of("alg", signedJWT.getHeader().getAlgorithm().getName()),
                    claims.getClaims());

        } catch (Exception e) {
            throw new JwtException("Error decoding JWT", e);
        }
    }
}
