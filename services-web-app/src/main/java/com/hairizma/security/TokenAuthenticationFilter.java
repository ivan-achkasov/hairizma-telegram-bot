package com.hairizma.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.hairizma.security.model.TokenAuthentication;
import com.hairizma.security.model.User;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public TokenAuthenticationFilter(final TokenAuthenticationManager tokenAuthenticationManager) {
        super("/*");
        setAuthenticationManager(tokenAuthenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException {
        final String token = getToken(request);

        if (token == null) {
            return TokenAuthentication.unauthenticated();
        }

        return getAuthenticationManager().authenticate(new TokenAuthentication(token, null, true, new User()));
    }

    private static String getToken(final HttpServletRequest request) {
        final String token = request.getHeader("token");
        if(StringUtils.isNotEmpty(token)) {
            return token;
        }

        return StringUtils.trimToNull(request.getParameter("token"));
    }

}
