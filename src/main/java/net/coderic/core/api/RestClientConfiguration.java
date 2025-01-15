package net.coderic.core.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizationFailureHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver;
import org.springframework.security.oauth2.client.web.client.SecurityContextHolderPrincipalResolver;
import org.springframework.web.client.RestClient;

/**
 * Configuration for providing a {@link RestClient} bean.
 *
 * @author Steve Riesenberg
 */
@Configuration
public class RestClientConfiguration {

    private final String baseUrl;

    public RestClientConfiguration(@Value("${messages.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Bean
    public RestClient restClient(OAuth2AuthorizedClientManager authorizedClientManager,
                                 OAuth2AuthorizedClientRepository authorizedClientRepository,
                                 OAuth2ClientHttpRequestInterceptor.ClientRegistrationIdResolver clientRegistrationIdResolver,
                                 OAuth2ClientHttpRequestInterceptor.PrincipalResolver principalResolver, RestClient.Builder builder) {

        OAuth2ClientHttpRequestInterceptor requestInterceptor = new OAuth2ClientHttpRequestInterceptor(
                authorizedClientManager);
        requestInterceptor.setClientRegistrationIdResolver(clientRegistrationIdResolver);
        requestInterceptor.setPrincipalResolver(principalResolver);

        OAuth2AuthorizationFailureHandler authorizationFailureHandler = OAuth2ClientHttpRequestInterceptor
                .authorizationFailureHandler(authorizedClientRepository);
        requestInterceptor.setAuthorizationFailureHandler(authorizationFailureHandler);

        return builder.baseUrl(this.baseUrl).requestInterceptor(requestInterceptor).build();
    }

    /**
     * This sample uses profiles to demonstrate additional strategies for resolving the
     * {@code clientRegistrationId}. See {@link ClientRegistrationIdResolverConfiguration}
     * for alternate implementations.
     * @return the default
     * {@link OAuth2ClientHttpRequestInterceptor.ClientRegistrationIdResolver}
     * @see ClientRegistrationIdResolverConfiguration
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2ClientHttpRequestInterceptor.ClientRegistrationIdResolver clientRegistrationIdResolver() {
        return new RequestAttributeClientRegistrationIdResolver();
    }

    /**
     * This sample uses profiles to demonstrate additional strategies for resolving the
     * {@code principal}. See {@link PrincipalResolverConfiguration} for alternate
     * implementations.
     * @return the default {@link OAuth2ClientHttpRequestInterceptor.PrincipalResolver}
     * @see PrincipalResolverConfiguration
     */
    @Bean
    @ConditionalOnMissingBean
    public OAuth2ClientHttpRequestInterceptor.PrincipalResolver principalResolver() {
        return new SecurityContextHolderPrincipalResolver();
    }

}