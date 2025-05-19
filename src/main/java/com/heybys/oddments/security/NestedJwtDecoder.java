package com.heybys.oddments.security;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.RemoteKeySourceException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NestedJwtDecoder implements JwtDecoder {
    private final JWTProcessor<SecurityContext> jwtProcessor;
    private final Converter<Map<String, Object>, Map<String, Object>> claimSetConverter =
            MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
    private final OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();

    public NestedJwtDecoder(JWKSet jwkSet) {
        this.jwtProcessor = createJwtProcessor(jwkSet);
    }

    private JWTProcessor<SecurityContext> createJwtProcessor(JWKSet jwkSet) {
        // JWKSource 생성
        JWKSource<SecurityContext> jwkSource = (jwkSelector, context) -> jwkSelector.select(jwkSet);

        // JWEDecryptionKeySelector 생성
        JWEDecryptionKeySelector<SecurityContext> decryptionKeySelector =
                new JWEDecryptionKeySelector<>(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM, jwkSource);

        // JWSVerificationKeySelector 생성
        JWSVerificationKeySelector<SecurityContext> verificationKeySelector =
                new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);

        // DefaultJWTProcessor 설정
        DefaultJWTProcessor<SecurityContext> defaultJWTProcessor = new DefaultJWTProcessor<>();
        defaultJWTProcessor.setJWETypeVerifier(DefaultJOSEObjectTypeVerifier.JOSE);
        defaultJWTProcessor.setJWEKeySelector(decryptionKeySelector);
        defaultJWTProcessor.setJWSKeySelector(verificationKeySelector);

        return defaultJWTProcessor;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        JWT jwt = this.parse(token);
        if (jwt instanceof PlainJWT) {
            log.trace("Failed to decode unsigned token");
            throw new BadJwtException(
                    "Unsupported algorithm of " + jwt.getHeader().getAlgorithm());
        } else {
            Jwt createdJwt = this.createJwt(token, jwt);
            return this.validateJwt(createdJwt);
        }
    }

    private JWT parse(String token) {
        try {
            return JWTParser.parse(token);
        } catch (Exception ex) {
            log.trace("Failed to parse token", ex);
            if (ex instanceof ParseException) {
                throw new BadJwtException(
                        String.format("An error occurred while attempting to decode the Jwt: %s", "Malformed token"),
                        ex);
            } else {
                throw new BadJwtException(
                        String.format("An error occurred while attempting to decode the Jwt: %s", ex.getMessage()), ex);
            }
        }
    }

    private Jwt createJwt(String token, JWT parsedJwt) {
        try {
            JWTClaimsSet jwtClaimsSet = this.jwtProcessor.process(parsedJwt, null);
            Map<String, Object> headers =
                    new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
            Map<String, Object> claims = this.claimSetConverter.convert(jwtClaimsSet.getClaims());
            return Jwt.withTokenValue(token)
                    .headers(h -> h.putAll(headers))
                    .claims(c -> c.putAll(claims))
                    .build();
        } catch (RemoteKeySourceException ex) {
            log.trace("Failed to retrieve JWK set", ex);
            if (ex.getCause() instanceof ParseException) {
                throw new JwtException(
                        String.format("An error occurred while attempting to decode the Jwt: %s", "Malformed Jwk set"),
                        ex);
            } else {
                throw new JwtException(
                        String.format("An error occurred while attempting to decode the Jwt: %s", ex.getMessage()), ex);
            }
        } catch (JOSEException ex) {
            log.trace("Failed to process JWT", ex);
            throw new JwtException(
                    String.format("An error occurred while attempting to decode the Jwt: %s", ex.getMessage()), ex);
        } catch (Exception ex) {
            log.trace("Failed to process JWT", ex);
            if (ex.getCause() instanceof ParseException) {
                throw new BadJwtException(
                        String.format("An error occurred while attempting to decode the Jwt: %s", "Malformed payload"),
                        ex);
            } else {
                throw new BadJwtException(
                        String.format("An error occurred while attempting to decode the Jwt: %s", ex.getMessage()), ex);
            }
        }
    }

    private Jwt validateJwt(Jwt jwt) {
        OAuth2TokenValidatorResult result = this.jwtValidator.validate(jwt);
        if (result.hasErrors()) {
            Collection<OAuth2Error> errors = result.getErrors();
            String validationErrorString = this.getJwtValidationExceptionMessage(errors);
            throw new JwtValidationException(validationErrorString, errors);
        } else {
            return jwt;
        }
    }

    private String getJwtValidationExceptionMessage(Collection<OAuth2Error> errors) {
        for (OAuth2Error oAuth2Error : errors) {
            if (StringUtils.hasLength(oAuth2Error.getDescription())) {
                return String.format(
                        "An error occurred while attempting to decode the Jwt: %s", oAuth2Error.getDescription());
            }
        }

        return "Unable to validate Jwt";
    }
}
