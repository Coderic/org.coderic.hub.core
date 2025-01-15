package net.coderic.core.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins(
                        "capacitor://localhost",
                        "ionic://localhost",
                        "http://localhost",
                        "http://localhost:8080",
                        "http://localhost:8100"
                )
                .allowedMethods(
                        "POST",
                        "GET",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "TRACE",
                        "CONNECT",
                        "OPTIONS"
                )
                .allowedHeaders("*") // En versiones posteriores debería definirse con mayor claridad las cabeceras específicas en lugar de un comodín
                .allowCredentials(true)
                .maxAge(3600);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                        "/webjars/**",
                        "/**"
                )
                .addResourceLocations(
                        "classpath:/META-INF/resources/webjars/",
                        "classpath:static/**"
                );
    }
}