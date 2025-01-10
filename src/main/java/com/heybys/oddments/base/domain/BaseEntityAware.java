package com.heybys.oddments.base.domain;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.heybys.oddments.security.UserPrincipal;

public interface BaseEntityAware {

    @JsonIgnore
    default String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && (authentication.getPrincipal()) instanceof UserPrincipal) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return String.valueOf(principal.getId());
        }
        return "SYSTEM";
    }

    @JsonIgnore
    default LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
