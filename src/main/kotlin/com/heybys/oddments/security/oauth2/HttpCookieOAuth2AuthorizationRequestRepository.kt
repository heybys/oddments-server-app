package com.heybys.oddments.security.oauth2

import com.heybys.oddments.util.CookieUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.util.Assert
import org.springframework.util.StringUtils

class HttpCookieOAuth2AuthorizationRequestRepository :
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        private const val COOKIE_EXPIRE_SECONDS = 180
    }

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        Assert.notNull(request, "request cannot be null")

        val stateParameter = getStateParameter(request) ?: return null
        val authorizationRequest = getAuthorizationRequest(request) ?: return null

        return if (stateParameter == authorizationRequest.state) {
            authorizationRequest
        } else {
            null
        }
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        Assert.notNull(request, "request cannot be null")
        Assert.notNull(response, "response cannot be null")

        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response)
        } else {
            val state = authorizationRequest.state
            Assert.hasText(state, "authorizationRequest.state cannot be empty")

            CookieUtils.addCookie(
                response,
                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtils.serialize(authorizationRequest),
                COOKIE_EXPIRE_SECONDS,
            )

            saveRedirectUri(request, response)
        }
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): OAuth2AuthorizationRequest? {
        Assert.notNull(response, "response cannot be null")

        val authorizationRequest = loadAuthorizationRequest(request)
        if (authorizationRequest != null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        }
        return authorizationRequest
    }

    private fun saveRedirectUri(request: HttpServletRequest, response: HttpServletResponse) {
        val redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (!StringUtils.hasText(redirectUriAfterLogin)) {
            return
        }
        CookieUtils.addCookie(
            response,
            REDIRECT_URI_PARAM_COOKIE_NAME,
            redirectUriAfterLogin,
            COOKIE_EXPIRE_SECONDS,
        )
    }

    private fun getStateParameter(request: HttpServletRequest): String? {
        return request.getParameter("state")
    }

    private fun getAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)?.let {
            CookieUtils.deserialize(it, OAuth2AuthorizationRequest::class.java)
        }
    }

    // private fun getRedirectUri(request: HttpServletRequest): String? {
    //     return CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
    //         ?.value
    // }
}
