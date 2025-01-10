package com.heybys.oddments.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.heybys.oddments.util.CookieUtils;

public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        String stateParameter = this.getStateParameter(request);
        if (stateParameter == null) {
            return null;
        } else {
            OAuth2AuthorizationRequest authorizationRequest = this.getAuthorizationRequest(request);
            return authorizationRequest != null && stateParameter.equals(authorizationRequest.getState())
                    ? authorizationRequest
                    : null;
        }
    }

    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response);
        } else {
            String state = authorizationRequest.getState();
            Assert.hasText(state, "authorizationRequest.state cannot be empty");
            CookieUtils.addCookie(
                    response,
                    OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                    CookieUtils.serialize(authorizationRequest),
                    COOKIE_EXPIRE_SECONDS);

            saveRedirectUri(request, response);
        }
    }

    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(response, "response cannot be null");

        OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request);
        if (authorizationRequest != null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        }
        return authorizationRequest;
    }

    private void saveRedirectUri(HttpServletRequest request, HttpServletResponse response) {
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (!StringUtils.hasText(redirectUriAfterLogin)) {
            return;
        }
        CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
    }

    private String getStateParameter(HttpServletRequest request) {
        return request.getParameter("state");
    }

    private OAuth2AuthorizationRequest getAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    // private String getRedirectUri(HttpServletRequest request) {
    //     return CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
    //             .map(Cookie::getValue)
    //             .orElse(null);
    // }
}
