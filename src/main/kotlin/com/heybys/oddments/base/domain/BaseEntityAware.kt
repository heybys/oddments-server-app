package com.heybys.oddments.base.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.heybys.oddments.security.OAuth2UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.LocalDateTime

interface BaseEntityAware {

    @get:JsonIgnore
    val currentAuditor: String
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return when (val principal = authentication?.principal) {
                is OAuth2UserPrincipal -> principal.getId().toString()
                is JwtAuthenticationToken -> principal.token.id.toString()
                else -> "SYSTEM"
            }
        }

    @get:JsonIgnore
    val now: LocalDateTime
        get() = LocalDateTime.now()
}
