package com.heybys.oddments.config

import com.heybys.oddments.security.NestedJwtDecoder
import com.heybys.oddments.security.NestedJwtEncoder
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Configuration
class KeyConfig {

    @Value("\${jwt.signing.public-key-location}")
    private lateinit var signingPublicKeyLocation: Resource

    @Value("\${jwt.signing.private-key-location}")
    private lateinit var signingPrivateKeyLocation: Resource

    @Value("\${jwt.encryption.public-key-location}")
    private lateinit var encryptionPublicKeyLocation: Resource

    @Value("\${jwt.encryption.private-key-location}")
    private lateinit var encryptionPrivateKeyLocation: Resource

    companion object {
        const val SIGNING_KEY = "signing-key"
        const val ENCRYPTION_KEY = "encryption-key"
    }

    @Throws(IOException::class)
    private fun readKey(resource: Resource): String {
        return Files.readString(Path.of(resource.uri))
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace(Regex("\\s+"), "")
    }

    @Bean
    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun signingPrivateKey(): RSAPrivateKey {
        val keyContent = readKey(signingPrivateKeyLocation)
        val keyBytes = Base64.getDecoder().decode(keyContent)

        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(keyBytes)) as RSAPrivateKey
    }

    @Bean
    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun signingPublicKey(): RSAPublicKey {
        val keyContent = readKey(signingPublicKeyLocation)
        val keyBytes = Base64.getDecoder().decode(keyContent)

        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(X509EncodedKeySpec(keyBytes)) as RSAPublicKey
    }

    @Bean
    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun encryptionPrivateKey(): RSAPrivateKey {
        val keyContent = readKey(encryptionPrivateKeyLocation)
        val keyBytes = Base64.getDecoder().decode(keyContent)

        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(keyBytes)) as RSAPrivateKey
    }

    @Bean
    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun encryptionPublicKey(): RSAPublicKey {
        val keyContent = readKey(encryptionPublicKeyLocation)
        val keyBytes = Base64.getDecoder().decode(keyContent)

        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(X509EncodedKeySpec(keyBytes)) as RSAPublicKey
    }

    @Bean
    fun jwkSet(
        signingPublicKey: RSAPublicKey,
        signingPrivateKey: RSAPrivateKey,
        encryptionPublicKey: RSAPublicKey,
        encryptionPrivateKey: RSAPrivateKey,
    ): JWKSet {
        val signingKey =
            RSAKey.Builder(signingPublicKey).privateKey(signingPrivateKey).keyID(SIGNING_KEY).build()

        val encryptionKey =
            RSAKey.Builder(encryptionPublicKey)
                .privateKey(encryptionPrivateKey)
                .keyID(ENCRYPTION_KEY)
                .build()

        return JWKSet(listOf(signingKey, encryptionKey))
    }

    @Bean
    fun jwtDecoder(jwkSet: JWKSet): JwtDecoder {
        return NestedJwtDecoder(jwkSet)
    }

    @Bean
    @Throws(JOSEException::class)
    fun jwtEncoder(jwkSet: JWKSet): JwtEncoder {
        return NestedJwtEncoder(jwkSet)
    }
}
