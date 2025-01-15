package net.coderic.core.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
        /*

                .requiresChannel(
                        channel -> channel
                                .anyRequest()
                                .requiresSecure()
                )
                */
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Origen permitido
        configuration.addAllowedMethod("*"); // Permitir todos los métodos
        configuration.addAllowedHeader("*"); // Permitir todos los encabezados
        configuration.getMaxAge();
        configuration.setAllowCredentials(false); // Permitir credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplicar configuración a todas las rutas
        return source;
    }
}