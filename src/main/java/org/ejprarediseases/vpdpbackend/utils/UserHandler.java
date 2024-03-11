package org.ejprarediseases.vpdpbackend.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class UserHandler {

    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public static boolean isAnonymous() {
        return (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    public static String getBearerToken() {
        String token = null;
        if (isAuthenticated() && !isAnonymous()) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            token = "Bearer " + ((JwtAuthenticationToken) authentication).getToken().getTokenValue();
        }
        return token;
    }
}
