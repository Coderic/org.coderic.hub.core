package net.coderic.core.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * {@inheritDoc}
     *
     * {@summary} Cargar página estática el contenido
     * Debe retirarse en el entorno producción, ya que es de uso exclusivo para depuración
     * @since 0.0.1
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
    /**
     * {@inheritDoc}
     *
     * {@summary} Configuración de intercambio de recursos de origen cruzado. CORS
     * Debe retirarse en el entorno producción, ya que es de uso exclusivo para depuración
     * @see <a href="https://developer.mozilla.org/es/docs/Web/HTTP/CORS">Control de acceso HTTP (CORS)</a>
     * @see <a href="https://www.baeldung.com/spring-cors">CORS with Spring</a>
     * @see <a href="https://spring.io/guides/gs/rest-service-cors/">Enabling Cross Origin Requests for a RESTful Web Service</a>
     * @since 0.0.1
     */
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