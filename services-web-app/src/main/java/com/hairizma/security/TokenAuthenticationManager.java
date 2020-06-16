package com.hairizma.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.hairizma.security.model.TokenAuthentication;
import com.hairizma.security.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;

public class TokenAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof TokenAuthentication) {
            return processAuthentication((TokenAuthentication) authentication);
        } else {
            authentication.setAuthenticated(false);
            return authentication;
        }
    }

    private TokenAuthentication processAuthentication(final TokenAuthentication authentication) throws AuthenticationException {
        final String token = authentication.getToken();
        final String key = "key123";
        final DefaultClaims claims;
        try {
            claims = (DefaultClaims) Jwts.parser().setSigningKey(key).parse(token).getBody();
        } catch (Exception ex) {
            throw new AuthenticationServiceException("Token corrupted");
        }
        if (claims.get("TOKEN_EXPIRATION_DATE", Long.class) == null) {
            throw new AuthenticationServiceException("Invalid token");
        }
        Date expiredDate = new Date(claims.get("TOKEN_EXPIRATION_DATE", Long.class));
        if (expiredDate.after(new Date())) {
            return buildFullTokenAuthentication(authentication, claims);
        } else {
            throw new AuthenticationServiceException("Token expired date error");
        }
    }

    private TokenAuthentication buildFullTokenAuthentication(final TokenAuthentication authentication, final DefaultClaims claims) {
        final User user = new User();       //(User) userDetailsService.loadUserByUsername(claims.get("USERNAME", String.class));
        user.setName("Ivan");
        if (true) {//user.isEnabled()) {
            final Collection<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("user"));//user.getAuthorities();

            return  new TokenAuthentication(authentication.getToken(), authorities, true, user);
        } else {
            throw new AuthenticationServiceException("User disabled");
        }
    }
}
