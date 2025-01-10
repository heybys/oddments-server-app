package com.heybys.oddments.security.oauth2;

import static com.heybys.oddments.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.heybys.oddments.exception.OAuth2AuthenticationProcessingException;
import com.heybys.oddments.util.CookieUtils;

public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtEncoder jwtEncoder;

    public OAuth2AuthenticationSuccessHandler(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        this.handle(request, response, authentication);
    }

    @Override
    protected String determineTargetUrl(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (this.isAlwaysUseDefaultTargetUrl()) {
            return this.getDefaultTargetUrl();
        }

        String redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(this.getDefaultTargetUrl());

        if (redirectUri.equals("/")) {
            throw new OAuth2AuthenticationProcessingException(
                    "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);

        // Claims 설정
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("example.com")
                .subject(authentication.getName())
                .audience(Collections.singletonList("example-audience"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim(
                        "scope",
                        authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())
                .build();

        // 헤더 설정
        JwsHeader headers = JwsHeader.with(SignatureAlgorithm.RS256).type("JWT").build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(headers, claims);

        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", jwt.getTokenValue())
                .build()
                .toUriString();
    }
}
