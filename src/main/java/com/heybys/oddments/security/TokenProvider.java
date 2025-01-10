package com.heybys.oddments.security;

import org.springframework.security.core.Authentication;

import com.sun.security.auth.UserPrincipal;

public class TokenProvider {

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Date now = new Date();
        // Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        // return Jwts.builder()
        //         .setSubject(Long.toString(userPrincipal.getId()))
        //         .setIssuedAt(new Date())
        //         .setExpiration(expiryDate)
        //         .compact();

        return "accessToken:" + userPrincipal.getName();
    }
}
