package net.coderic.core.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor.ClientRegistrationIdResolver;
import org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver;


@Configuration
public class ClientRegistrationIdResolverConfiguration {

    private static OAuth2ClientHttpRequestInterceptor.ClientRegistrationIdResolver currentUserClientRegistrationIdResolver() {
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        return (request) -> {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return (authentication instanceof OAuth2AuthenticationToken principal)
                    ? principal.getAuthorizedClientRegistrationId() : null;
        };
    }

    private static ClientRegistrationIdResolver compositeClientRegistrationIdResolver(
            String defaultClientRegistrationId) {
        ClientRegistrationIdResolver requestAttributes = new RequestAttributeClientRegistrationIdResolver();
        ClientRegistrationIdResolver currentUser = currentUserClientRegistrationIdResolver();
        return (request) -> {
            String clientRegistrationId = requestAttributes.resolve(request);
            if (clientRegistrationId == null) {
                clientRegistrationId = currentUser.resolve(request);
            }
            if (clientRegistrationId == null) {
                clientRegistrationId = defaultClientRegistrationId;
            }
            return clientRegistrationId;
        };
    }

    private static ClientRegistrationIdResolver authenticationRequiredClientRegistrationIdResolver() {
        ClientRegistrationIdResolver currentUser = currentUserClientRegistrationIdResolver();
        return (request) -> {
            String clientRegistrationId = currentUser.resolve(request);
            if (clientRegistrationId == null) {
                throw new AccessDeniedException(
                        "Authentication with OAuth 2.0 or OpenID Connect 1.0 Login is required");
            }
            return clientRegistrationId;
        };
    }

    @Configuration
    @Profile("current-user")
    public static class CurrentUserClientRegistrationIdResolverConfiguration {

        @Bean
        public ClientRegistrationIdResolver clientRegistrationIdResolver() {
            return currentUserClientRegistrationIdResolver();
        }

    }

    @Configuration
    @Profile("composite")
    public static class CompositeClientRegistrationIdResolverConfiguration {

        @Bean
        public ClientRegistrationIdResolver clientRegistrationIdResolver() {
            return compositeClientRegistrationIdResolver("messaging-client");
        }

    }

    @Configuration
    @Profile("authentication-required")
    public static class AuthenticationRequiredClientRegistrationIdResolverConfiguration {

        @Bean
        public ClientRegistrationIdResolver clientRegistrationIdResolver() {
            return authenticationRequiredClientRegistrationIdResolver();
        }

    }

}