package com.heybys.oddments.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncodingException;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class NestedJwtEncoder implements JwtEncoder {
    private final RSAPrivateKey signingKey;
    private final RSAPublicKey encryptionKey;

    public NestedJwtEncoder(RSAPrivateKey signingKey, RSAPublicKey encryptionKey) {
        this.signingKey = signingKey;
        this.encryptionKey = encryptionKey;
    }

    @Override
    public Jwt encode(JwtEncoderParameters parameters) throws JwtEncodingException {
        try {
            // Spring Security의 JwtClaimsSet을 Nimbus의 JWTClaimsSet으로 변환
            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();

            JwtClaimsSet springClaims = parameters.getClaims();
            Map<String, Object> claims = springClaims.getClaims();

            // 기본 클레임들 설정
            claims.forEach((key, value) -> {
                if (value != null) {
                    claimsBuilder.claim(key, value);
                }
            });

            // registered claims 특별 처리
            if (springClaims.getIssuedAt() != null) {
                claimsBuilder.issueTime(Date.from(springClaims.getIssuedAt()));
            }
            if (springClaims.getExpiresAt() != null) {
                claimsBuilder.expirationTime(Date.from(springClaims.getExpiresAt()));
            }
            if (springClaims.getSubject() != null) {
                claimsBuilder.subject(springClaims.getSubject());
            }
            // 필요한 경우 다른 registered claims도 추가

            // 1. Create signed JWT (JWS)
            SignedJWT signedJWT =
                    new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).build(), claimsBuilder.build());

            // 2. Sign the JWT
            signedJWT.sign(new RSASSASigner(signingKey));

            // 3. Create encrypted JWT (JWE)
            JWEObject jweObject = new JWEObject(
                    new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                            .contentType("JWT") // 중첩된 JWT임을 표시
                            .build(),
                    new Payload(signedJWT));

            // 4. Encrypt the JWS
            jweObject.encrypt(new RSAEncrypter(encryptionKey));

            // 5. Serialize the nested JWT
            String token = jweObject.serialize();

            return new Jwt(
                    token,
                    parameters.getClaims().getIssuedAt(),
                    parameters.getClaims().getExpiresAt(),
                    Map.of("alg", "RSA-OAEP-256"),
                    parameters.getClaims().getClaims());

        } catch (Exception e) {
            throw new JwtEncodingException("Error encoding JWT", e);
        }
    }
}
