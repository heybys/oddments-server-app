package com.heybys.oddments.security

import com.heybys.oddments.config.KeyConfig
import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.RSAEncrypter
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwtEncodingException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date

class NestedJwtEncoder(jwkSet: JWKSet) : JwtEncoder {

    private val signingKey: RSAPrivateKey
    private val encryptionKey: RSAPublicKey

    init {
        try {
            signingKey = jwkSet.getKeyByKeyId(KeyConfig.SIGNING_KEY).toRSAKey().toRSAPrivateKey()
            encryptionKey = jwkSet.getKeyByKeyId(KeyConfig.ENCRYPTION_KEY).toRSAKey().toRSAPublicKey()
        } catch (e: JOSEException) {
            throw JwtEncodingException("Error initializing keys", e)
        }
    }

    @Throws(JwtEncodingException::class)
    override fun encode(parameters: JwtEncoderParameters): Jwt {
        try {
            // Spring Security 의 JwtClaimsSet 을 Nimbus 의 JWTClaimsSet 으로 변환
            val claimsBuilder = JWTClaimsSet.Builder()

            val springClaims = parameters.claims
            val claims = springClaims.claims

            // 기본 클레임들 설정
            claims.forEach { (key, value) -> value?.let { claimsBuilder.claim(key, it) } }

            // registered claims 특별 처리
            springClaims.issuedAt?.let { claimsBuilder.issueTime(Date.from(it)) }
            springClaims.expiresAt?.let { claimsBuilder.expirationTime(Date.from(it)) }
            springClaims.subject?.let { claimsBuilder.subject(it) }

            // 1. Create signed JWT (JWS)
            val signedJWT =
                SignedJWT(
                    JWSHeader.Builder(JWSAlgorithm.RS256)
                        .type(JOSEObjectType.JWT)
                        .keyID(KeyConfig.SIGNING_KEY)
                        .build(),
                    claimsBuilder.build(),
                )

            // 2. Sign the JWT
            signedJWT.sign(RSASSASigner(signingKey))

            // 3. Create encrypted JWT (JWE)
            val jweObject =
                JWEObject(
                    JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                        .keyID(KeyConfig.ENCRYPTION_KEY)
                        .type(JOSEObjectType.JOSE)
                        .contentType(JOSEObjectType.JWT.type)
                        .build(),
                    Payload(signedJWT),
                )

            // 4. Encrypt the JWS
            jweObject.encrypt(RSAEncrypter(encryptionKey))

            // 5. Serialize the nested JWT
            val token = jweObject.serialize()

            return Jwt(
                token,
                parameters.claims.issuedAt,
                parameters.claims.expiresAt,
                mapOf("alg" to "RSA-OAEP-256"),
                parameters.claims.claims,
            )
        } catch (e: Exception) {
            throw JwtEncodingException("Error encoding JWT", e)
        }
    }
}
