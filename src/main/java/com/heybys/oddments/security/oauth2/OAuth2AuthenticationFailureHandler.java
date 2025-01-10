package com.heybys.oddments.security.oauth2;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String failureRedirectUrl = "/login";

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String referer = request.getHeader(HttpHeaders.REFERER);
        if (referer == null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Sending 401 Unauthorized error since no failure URL is set");
            } else {
                this.logger.debug("Sending 401 Unauthorized error");
            }
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } else {
            this.saveException(request, exception);
            if (this.isUseForward()) {
                this.logger.debug("Forwarding to " + this.getDefaultFailureUrl(request, exception));
                request.getRequestDispatcher(this.getDefaultFailureUrl(request, exception))
                        .forward(request, response);
            } else {
                this.getRedirectStrategy()
                        .sendRedirect(request, response, this.getDefaultFailureUrl(request, exception));
            }
        }
    }

    private String getDefaultFailureUrl(HttpServletRequest request, AuthenticationException exception) {
        String referer = request.getHeader(HttpHeaders.REFERER);
        return UriComponentsBuilder.fromUriString(referer)
                .path(failureRedirectUrl)
                .queryParam("errorMessage", exception.getMessage())
                .build()
                .toUriString();
    }
}
