package com.heybys.oddments.security.oauth2.user

import com.heybys.oddments.exception.OAuth2AuthenticationProcessingException
import com.heybys.oddments.fooddelivery.domain.generic.AuthProvider

object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
        return when (registrationId.uppercase()) {
            AuthProvider.GOOGLE.toString() -> GoogleOAuth2UserInfo(attributes)
            AuthProvider.FACEBOOK.toString() -> FacebookOAuth2UserInfo(attributes)
            AuthProvider.GITHUB.toString() -> GithubOAuth2UserInfo(attributes)
            else ->
                throw OAuth2AuthenticationProcessingException(
                    "Sorry! Login with $registrationId is not supported yet.",
                )
        }
    }
}
