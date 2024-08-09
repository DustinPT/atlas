package org.apache.atlas.authorize.custom;

import org.apache.atlas.authorize.simple.AtlasSimpleAuthorizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collection;
import java.util.Set;

public class AtlasCustomAuthorizer extends AtlasSimpleAuthorizer {
    protected Set<String> getRoles(String userName, Set<String> userGroups) {
        Set<String> roles = super.getRoles(userName, userGroups);
        OAuth2UserAuthority authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().filter(OAuth2UserAuthority.class::isInstance)
                .map(OAuth2UserAuthority.class::cast).findFirst().orElse(null);
        if (authority == null) {
            return roles;
        }
        Collection<String> userRoles = (Collection<String>) authority.getAttributes().get("roles");
        if (roles != null) {
            roles.addAll(userRoles);
        }
        return roles;
    }
}
