/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
