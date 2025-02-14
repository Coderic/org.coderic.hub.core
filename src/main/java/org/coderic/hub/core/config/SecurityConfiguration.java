package org.coderic.hub.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwksUri;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        "/",
                        "/home.html",
                        "/public/**",
                        "/error",
                        "/swagger-ui/home.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
        )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(10)
                )
        .headers(headers -> headers
            .httpStrictTransportSecurity((hsts) -> hsts
                    .includeSubDomains(true)
                    .preload(true)
                    .maxAgeInSeconds(31536000)
            )
            .addHeaderWriter(
                    new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.DENY)
            )
        )

        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                    .jwkSetUri(jwksUri)
            )
        )
        .requiresChannel(
                channel -> channel
                .anyRequest()
                .requiresSecure()
        );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }
}