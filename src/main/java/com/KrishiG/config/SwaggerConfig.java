package com.KrishiG.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("KrishiG Application")
                        .description("This is the KrishiG API developed by KrishiG-Team")
                        .version("1.0")
                        .contact(new Contact().name("KrishiG").email("krishigdgs@gmail.com").url("tempUrl"))
                        .license(new License().name("KrishiG Licence")))
                .externalDocs(new ExternalDocumentation().url("krishiG.com").description("This is external URL"));
    }
}
