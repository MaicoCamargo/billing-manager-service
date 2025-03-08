package com.infra4u.billing_manager_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApi {
    @Value("${openapi.url.dev}")
    private String devUrl;

    @Value("${openapi.url.prod}")
    private String prodUrl;

    /**
     * @apiNote <a href="https://www.bezkoder.com/spring-boot-swagger-3/">...</a>
     * @return OpenAPI
     */
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("maicocamargo.dev@gmail.com");
        contact.setName("Maico Camargo");
        contact.setUrl("https://github.com/maicocamargo");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Billing Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage billing.")
                .termsOfService("")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
