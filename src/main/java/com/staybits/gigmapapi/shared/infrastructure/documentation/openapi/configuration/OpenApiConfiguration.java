package com.staybits.gigmapapi.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Value("GigMap-API-V1")
    String applicationName;

    @Value("@project.description@")
    String applicationDescription;

    @Value("@project.version@")
    String applicationVersion;

    @Bean
    public OpenAPI learninfPlatformOpenApi() {

        // Define JWT Security Scheme
        final String securitySchemeName = "bearerAuth";
        
        //General configuration
        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription + "\n\n" +
                                "**Authentication:** Use the 'Authorize' button to enter your JWT token.\n" +
                                "1. Login via `/api/v1/auth/login` to get your token\n" +
                                "2. Click 'Authorize' button (top right)\n" +
                                "3. Enter your token (no need to add 'Bearer ' prefix)\n" +
                                "4. Click 'Authorize' and 'Close'\n" +
                                "5. Now you can access protected endpoints!")
                        .version(this.applicationVersion)
                        .license(new License().name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("GigMap api")
                        .url(""))
                // Add JWT Security Configuration
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /api/v1/auth/login")));

        return openApi;
    }
}
