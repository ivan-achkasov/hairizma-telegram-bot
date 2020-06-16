package com.hairizma.security.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class TokenAuthentication implements Authentication {
    private final String token;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated;
    private final User principal;

    public static TokenAuthentication unauthenticated() {
        return new TokenAuthentication(null, Collections.emptyList(), false, null);
    }

    public TokenAuthentication() {
        token = null;
        authorities = Collections.emptyList();
        isAuthenticated = false;
        principal = null;
    }

    public TokenAuthentication(final String token,
                               final Collection<SimpleGrantedAuthority> authorities,
                               final boolean isAuthenticated,
                               final User principal) {
        this.token = token;
        this.authorities = authorities;
        this.isAuthenticated = isAuthenticated;
        this.principal = principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getName() {
        return principal.getName();
    }
}

