package com.heybys.oddments.security.oauth2

import com.heybys.oddments.exception.OAuth2AuthenticationProcessingException
import com.heybys.oddments.fooddelivery.domain.generic.AuthProvider
import com.heybys.oddments.fooddelivery.domain.user.User
import com.heybys.oddments.fooddelivery.domain.user.UserRepository
import com.heybys.oddments.security.OAuth2UserPrincipal
import com.heybys.oddments.security.oauth2.user.OAuth2UserInfoFactory
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.util.StringUtils

class OAuth2UserProcessingService(private val userRepository: UserRepository) :
    DefaultOAuth2UserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        return try {
            processOAuth2User(userRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the
            // OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(
        oAuth2UserRequest: OAuth2UserRequest,
        oAuth2User: OAuth2User,
    ): OAuth2User {
        val oAuth2UserInfo =
            OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.clientRegistration.registrationId,
                oAuth2User.attributes,
            )

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        val userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail()!!)
        val user =
            if (userOptional.isPresent) {
                val existingUser = userOptional.get()
                val expectedProvider =
                    AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId.uppercase())

                if (existingUser.provider != expectedProvider) {
                    throw OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with ${existingUser.provider} account. " +
                            "Please use your ${existingUser.provider} account to login.",
                    )
                }
                updateExistingUser(existingUser, oAuth2UserInfo)
            } else {
                registerNewUser(oAuth2UserRequest, oAuth2UserInfo)
            }

        return OAuth2UserPrincipal.create(user, oAuth2User.attributes)
    }

    private fun registerNewUser(
        oAuth2UserRequest: OAuth2UserRequest,
        oAuth2UserInfo: com.heybys.oddments.security.oauth2.user.OAuth2UserInfo,
    ): User {
        val user =
            User().apply {
                name = oAuth2UserInfo.getName()
                email = oAuth2UserInfo.getEmail()
                imageUrl = oAuth2UserInfo.getImageUrl()
                provider =
                    AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId.uppercase())
                providerId = oAuth2UserInfo.getId()
            }
        return userRepository.save(user)
    }

    private fun updateExistingUser(
        existingUser: User,
        oAuth2UserInfo: com.heybys.oddments.security.oauth2.user.OAuth2UserInfo,
    ): User {
        existingUser.apply {
            name = oAuth2UserInfo.getName()
            imageUrl = oAuth2UserInfo.getImageUrl()
        }
        return userRepository.save(existingUser)
    }
}
