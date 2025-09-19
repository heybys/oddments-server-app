package com.heybys.oddments.config

import com.heybys.oddments.fooddelivery.domain.user.UserRepository
import com.heybys.oddments.security.CustomAuthenticationEntryPoint
import com.heybys.oddments.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.heybys.oddments.security.oauth2.OAuth2AuthenticationFailureHandler
import com.heybys.oddments.security.oauth2.OAuth2AuthenticationSuccessHandler
import com.heybys.oddments.security.oauth2.OAuth2UserProcessingService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(
    private val userRepository: UserRepository,
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
) {

    fun jwtAuthenticationConverter(): Converter<Jwt, out AbstractAuthenticationToken> {
        val jwtGrantedAuthoritiesConverter =
            JwtGrantedAuthoritiesConverter().apply { setAuthorityPrefix("") }

        return JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)
        }
    }

    @Bean
    fun authorizationRequestRepository(): AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
        return HttpCookieOAuth2AuthorizationRequestRepository()
    }

    @Bean
    fun oAuth2UserProcessingService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return OAuth2UserProcessingService(userRepository)
    }

    @Bean
    fun oAuth2AuthenticationSuccessHandler(): AuthenticationSuccessHandler {
        return OAuth2AuthenticationSuccessHandler(jwtEncoder)
    }

    @Bean
    fun authenticationFailureHandler(): AuthenticationFailureHandler {
        return OAuth2AuthenticationFailureHandler().apply { setAllowSessionCreation(false) }
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return CustomAuthenticationEntryPoint()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf(AbstractHttpConfigurer<*, *>::disable)
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/v1/**", "/oauth2/**").permitAll().anyRequest().authenticated()
            }
            .httpBasic(AbstractHttpConfigurer<*, *>::disable)
            .formLogin(AbstractHttpConfigurer<*, *>::disable)
            .anonymous(AbstractHttpConfigurer<*, *>::disable)
            .oauth2Login { oAuth2LoginConfigurer ->
                oAuth2LoginConfigurer
                    .authorizationEndpoint { authEndpointConfig ->
                        authEndpointConfig.authorizationRequestRepository(authorizationRequestRepository())
                    }
                    .redirectionEndpoint { redirectionEndpointConfig ->
                        redirectionEndpointConfig.baseUri("/oauth2/callback/*")
                    }
                    .userInfoEndpoint { userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(oAuth2UserProcessingService())
                    }
                    .successHandler(oAuth2AuthenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler())
            }
            .oauth2ResourceServer { oAuth2ResourceServerConfigurer ->
                oAuth2ResourceServerConfigurer.jwt { jwtConfigurer ->
                    jwtConfigurer
                        .decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
            .sessionManagement { sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration =
            CorsConfiguration().apply {
                allowedOriginPatterns = listOf("*")
                allowedHeaders = listOf("*")
                allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                allowCredentials = true
            }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}
