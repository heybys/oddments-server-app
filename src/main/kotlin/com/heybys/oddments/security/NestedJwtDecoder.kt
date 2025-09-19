package com.heybys.oddments.security

import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.RemoteKeySourceException
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier
import com.nimbusds.jose.proc.JWEDecryptionKeySelector
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.PlainJWT
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.nimbusds.jwt.proc.JWTProcessor
import mu.KotlinLogging
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.JwtValidationException
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter
import org.springframework.util.StringUtils
import java.text.ParseException
import java.util.LinkedHashMap

private val logger = KotlinLogging.logger {}

class NestedJwtDecoder(jwkSet: JWKSet) : JwtDecoder {

    private val jwtProcessor: JWTProcessor<SecurityContext> = createJwtProcessor(jwkSet)
    private val claimSetConverter: Converter<Map<String, Any>, Map<String, Any>> =
        MappedJwtClaimSetConverter.withDefaults(emptyMap())
    private val jwtValidator: OAuth2TokenValidator<Jwt> = JwtValidators.createDefault()

    private fun createJwtProcessor(jwkSet: JWKSet): JWTProcessor<SecurityContext> {
        // JWKSource 생성
        val jwkSource = JWKSource<SecurityContext> { jwkSelector, _ -> jwkSelector.select(jwkSet) }

        // JWEDecryptionKeySelector 생성
        val decryptionKeySelector =
            JWEDecryptionKeySelector(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM, jwkSource)

        // JWSVerificationKeySelector 생성
        val verificationKeySelector = JWSVerificationKeySelector(JWSAlgorithm.RS256, jwkSource)

        // DefaultJWTProcessor 설정
        return DefaultJWTProcessor<SecurityContext>().apply {
            jweTypeVerifier = DefaultJOSEObjectTypeVerifier.JOSE
            jweKeySelector = decryptionKeySelector
            jwsKeySelector = verificationKeySelector
        }
    }

    @Throws(JwtException::class)
    override fun decode(token: String): Jwt {
        val jwt = parse(token)
        return if (jwt is PlainJWT) {
            logger.trace { "Failed to decode unsigned token" }
            throw BadJwtException("Unsupported algorithm of ${jwt.header.algorithm}")
        } else {
            val createdJwt = createJwt(token, jwt)
            validateJwt(createdJwt)
        }
    }

    private fun parse(token: String): JWT {
        return try {
            JWTParser.parse(token)
        } catch (ex: Exception) {
            logger.trace(ex) { "Failed to parse token" }
            val message =
                when (ex) {
                    is ParseException -> "Malformed token"
                    else -> ex.message ?: "Unknown error"
                }
            throw BadJwtException("An error occurred while attempting to decode the Jwt: $message", ex)
        }
    }

    private fun createJwt(token: String, parsedJwt: JWT): Jwt {
        return try {
            val jwtClaimsSet = jwtProcessor.process(parsedJwt, null)
            val headers = LinkedHashMap(parsedJwt.header.toJSONObject())
            val claims = claimSetConverter.convert(jwtClaimsSet.claims) ?: emptyMap()

            Jwt.withTokenValue(token)
                .headers { h -> h.putAll(headers) }
                .claims { c -> c.putAll(claims) }
                .build()
        } catch (ex: RemoteKeySourceException) {
            logger.trace(ex) { "Failed to retrieve JWK set" }
            val message =
                if (ex.cause is ParseException) {
                    "Malformed Jwk set"
                } else {
                    ex.message ?: "Remote key source error"
                }
            throw JwtException("An error occurred while attempting to decode the Jwt: $message", ex)
        } catch (ex: JOSEException) {
            logger.trace { "Failed to process JWT: ${ex.message}" }
            throw JwtException("An error occurred while attempting to decode the Jwt: ${ex.message}", ex)
        } catch (ex: Exception) {
            logger.trace { "Failed to process JWT: ${ex.message}" }
            val message =
                if (ex.cause is ParseException) "Malformed payload" else ex.message ?: "Processing error"
            throw BadJwtException("An error occurred while attempting to decode the Jwt: $message", ex)
        }
    }

    private fun validateJwt(jwt: Jwt): Jwt {
        val result = jwtValidator.validate(jwt)
        return if (result.hasErrors()) {
            val errors = result.errors
            val validationErrorString = getJwtValidationExceptionMessage(errors)
            throw JwtValidationException(validationErrorString, errors)
        } else {
            jwt
        }
    }

    private fun getJwtValidationExceptionMessage(errors: Collection<OAuth2Error>): String {
        for (oAuth2Error in errors) {
            if (StringUtils.hasLength(oAuth2Error.description)) {
                return "An error occurred while attempting to decode the Jwt: ${oAuth2Error.description}"
            }
        }
        return "Unable to validate Jwt"
    }
}
