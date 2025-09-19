package com.heybys.oddments.security

import com.heybys.oddments.fooddelivery.domain.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2UserPrincipal(
    private val id: Long,
    private val name: String,
    private val email: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>,
    private val attributes: Map<String, Any>?,
) : OAuth2User, UserDetails {

    companion object {
        fun create(user: User): OAuth2UserPrincipal {
            return create(user, null)
        }

        fun create(user: User, attributes: Map<String, Any>?): OAuth2UserPrincipal {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

            return OAuth2UserPrincipal(
                user.getId()?.longValue() ?: 0L,
                user.name ?: "",
                user.email ?: "",
                user.password ?: "",
                authorities,
                attributes,
            )
        }
    }

    fun getId(): Long = id

    override fun getName(): String = name

    override fun getAttributes(): Map<String, Any>? = attributes

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getUsername(): String = email

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
