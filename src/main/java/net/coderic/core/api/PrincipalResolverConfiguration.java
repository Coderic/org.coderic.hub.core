package net.coderic.core.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor.PrincipalResolver;
import org.springframework.security.oauth2.client.web.client.RequestAttributePrincipalResolver;
import org.springframework.security.oauth2.client.web.client.SecurityContextHolderPrincipalResolver;

public class PrincipalResolverConfiguration {

    @Configuration
    @Profile("per-request")
    public static class PerRequestPrincipalResolverConfiguration {

        @Bean
        public PrincipalResolver principalResolver() {
            return new RequestAttributePrincipalResolver();
        }

    }

    @Configuration
    @Profile("anonymous-user")
    public static class AnonymousUserPrincipalResolverConfiguration {

        @Bean
        public PrincipalResolver principalResolver() {
            AnonymousAuthenticationToken anonymousUser = new AnonymousAuthenticationToken("anonymous", "anonymousUser",
                    AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
            return (request) -> anonymousUser;
        }

    }

}