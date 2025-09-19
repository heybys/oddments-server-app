package com.heybys.oddments.security.oauth2

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

class OAuth2AuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {

    private val failureRedirectUrl = "/login"

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        val referer = request.getHeader(HttpHeaders.REFERER)
        if (referer == null) {
            if (logger.isTraceEnabled) {
                logger.trace("Sending 401 Unauthorized error since no failure URL is set")
            } else {
                logger.debug("Sending 401 Unauthorized error")
            }
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
        } else {
            saveException(request, exception)
            if (isUseForward) {
                logger.debug("Forwarding to ${getDefaultFailureUrl(request, exception)}")
                request
                    .getRequestDispatcher(getDefaultFailureUrl(request, exception))
                    .forward(request, response)
            } else {
                redirectStrategy.sendRedirect(request, response, getDefaultFailureUrl(request, exception))
            }
        }
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
