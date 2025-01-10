package com.heybys.oddments.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final String failureRedirectUrl = "/login";
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        String redirectUrl = this.getDefaultFailureUrl(request, authException);
        this.redirectStrategy.sendRedirect(request, response, redirectUrl);
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
