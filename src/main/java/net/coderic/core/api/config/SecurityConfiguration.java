package net.coderic.core.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
        .cors(AbstractHttpConfigurer::disable)
                //.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

                .oauth2Client(Customizer.withDefaults())

        .oauth2Login((oauth2Login) -> oauth2Login
                //.loginPage()
                        .loginProcessingUrl("/swagger-ui/index.html")

                //.loginPage(this.loginPage)
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
        /*.logout(logout -> logout
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
        )*/
        .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/index"))
        .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login"))
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
        .requiresChannel(
                channel -> channel
                        .anyRequest()
                        .requiresSecure()
        );
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://example.com", "http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","HEAD","OPTIONS"));
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Authorization");
        configuration.getMaxAge();
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // allow all paths
        return source;
    }
}