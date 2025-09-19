package com.heybys.oddments.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val failureRedirectUrl = "/login"
    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        val redirectUrl = getDefaultFailureUrl(request, authException)
        redirectStrategy.sendRedirect(request, response, redirectUrl)
    }

    private fun getDefaultFailureUrl(
        request: HttpServletRequest,
        exception: AuthenticationException,
    ): String {
        val referer = request.getHeader(HttpHeaders.REFERER)
        return UriComponentsBuilder.fromUriString(referer)
            .path(failureRedirectUrl)
            .queryParam("errorMessage", exception.message)
            .build()
            .toUriString()
    }
}
