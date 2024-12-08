package com.github.fabioper.newsletter.infra.services.identity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdentityService {
    public User getActiveUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var jwt = (Jwt) auth.getPrincipal();

        var userId = UUID.fromString(jwt.getSubject());

        var authorities = auth.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority);

        var roles = authorities
            .filter(aut -> aut.startsWith("ROLE_"))
            .map(aut -> aut.replace("ROLE_", ""))
            .toList();

        return new User(userId, roles);
    }
}
