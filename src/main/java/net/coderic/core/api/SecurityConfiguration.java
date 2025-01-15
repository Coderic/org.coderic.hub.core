package net.coderic.core.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final String loginPage;

    public SecurityConfiguration(@Value("${app.login-page}") String loginPage) {
        this.loginPage = loginPage;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
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
                .anyRequest().authenticated()
        )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(10)
                )
        .oauth2Login((oauth2Login) -> oauth2Login
                .loginPage(this.loginPage)
        )
                /*
        .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .permitAll()
        )*/
        .logout(logout -> logout
                .addLogoutHandler(
                        new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(
                                        ClearSiteDataHeaderWriter.Directive.CACHE,
                                        ClearSiteDataHeaderWriter.Directive.COOKIES
                                )
                        )
                )
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies()
                .permitAll()
        )
        .oauth2Client(Customizer.withDefaults())
        /*.headers(
                        headers -> headers
                                .httpStrictTransportSecurity(
                                        hsts -> hsts
                                                .includeSubDomains(true)
                                                .preload(true)
                                                .maxAgeInSeconds(31536000)
                                )
                                .addHeaderWriter(
                                        new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.DENY)
                                )
                )
                */

                .requiresChannel(
                        channel -> channel
                                .anyRequest()
                                .requiresSecure()
                );

    ;

        return http.build();
    }
}