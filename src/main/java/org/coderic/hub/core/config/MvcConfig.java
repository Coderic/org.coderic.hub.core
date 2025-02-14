package org.coderic.hub.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

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