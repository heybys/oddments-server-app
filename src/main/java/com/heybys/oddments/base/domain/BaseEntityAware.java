package com.heybys.oddments.base.domain;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heybys.oddments.security.OAuth2UserPrincipal;

public interface BaseEntityAware {

    @JsonIgnore
    default String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && (authentication.getPrincipal()) instanceof OAuth2UserPrincipal) {
            OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
            return String.valueOf(principal.getId());
        }
        if (authentication != null && (authentication.getPrincipal()) instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) authentication.getPrincipal();
            return String.valueOf(token.getToken().getId());
        }
        return "SYSTEM";
    }

    @JsonIgnore
    default LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
