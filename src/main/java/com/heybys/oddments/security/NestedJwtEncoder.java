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

import com.heybys.oddments.config.KeyConfig;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class NestedJwtEncoder implements JwtEncoder {

    private final RSAPrivateKey signingKey;
    private final RSAPublicKey encryptionKey;

    public NestedJwtEncoder(JWKSet jwkSet) throws JOSEException {
        this.signingKey = jwkSet.getKeyByKeyId(KeyConfig.SIGNING_KEY).toRSAKey().toRSAPrivateKey();
        this.encryptionKey =
                jwkSet.getKeyByKeyId(KeyConfig.ENCRYPTION_KEY).toRSAKey().toRSAPublicKey();
    }

    @Override
    public Jwt encode(JwtEncoderParameters parameters) throws JwtEncodingException {
        try {
            // Spring Security 의 JwtClaimsSet 을 Nimbus 의 JWTClaimsSet 으로 변환
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

            // 1. Create signed JWT (JWS)
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .type(JOSEObjectType.JWT)
                            .keyID(KeyConfig.SIGNING_KEY)
                            .build(),
                    claimsBuilder.build());

            // 2. Sign the JWT
            signedJWT.sign(new RSASSASigner(signingKey));

            // 3. Create encrypted JWT (JWE)
            JWEObject jweObject = new JWEObject(
                    new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                            .keyID(KeyConfig.ENCRYPTION_KEY)
                            .type(JOSEObjectType.JOSE)
                            .contentType(JOSEObjectType.JWT.getType())
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
