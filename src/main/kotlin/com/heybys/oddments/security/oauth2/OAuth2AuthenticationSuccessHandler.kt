package com.heybys.oddments.security.oauth2

import com.heybys.oddments.exception.OAuth2AuthenticationProcessingException
import com.heybys.oddments.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import com.heybys.oddments.util.CookieUtils
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.time.Instant

class OAuth2AuthenticationSuccessHandler(private val jwtEncoder: JwtEncoder) :
    SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        handle(request, response, authentication)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ): String {
        if (isAlwaysUseDefaultTargetUrl) {
            return defaultTargetUrl
        }

        val redirectUri =
            CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.let { it.value }
                ?: defaultTargetUrl

        if (redirectUri == "/") {
            throw OAuth2AuthenticationProcessingException(
                "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication",
            )
        }
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)

        // Claims 설정
        val claims =
            JwtClaimsSet.builder()
                .issuer("example.com")
                .subject(authentication.name)
                .audience(listOf("example-audience"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .claim("scope", authentication.authorities.map(GrantedAuthority::getAuthority))
                .build()

        // 헤더 설정
        val headers = JwsHeader.with(SignatureAlgorithm.RS256).type("JWT").build()
        val jwtEncoderParameters = JwtEncoderParameters.from(headers, claims)

        val jwt = jwtEncoder.encode(jwtEncoderParameters)

        return UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("accessToken", jwt.tokenValue)
            .build()
            .toUriString()
    }
}
