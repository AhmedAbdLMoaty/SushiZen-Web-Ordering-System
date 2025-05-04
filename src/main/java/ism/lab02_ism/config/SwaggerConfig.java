package ism.lab02_ism.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sushiZenOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SushiZen API")
                        .description("API for SushiZen Web Ordering System")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("X-User-ID", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-ID")
                                .description("User ID for testing different user accounts (1, 2, admin1, kitchen1)"))
                        .addSecuritySchemes("X-User-Role", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Role")
                                .description("User role for testing (ADMIN, KITCHEN_STAFF)")))
                .tags(Arrays.asList(
                        new Tag().name("Statistics").description("API usage statistics")));
    }
}