package com.example.Devkor_project.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig
{
    @Autowired
    VersionProvider versionProvider;

    // Swagger 기본 설정
    @Bean
    public OpenAPI openAPI()
    {
        Info info = new Info()
                .title("ACADEM-API")
                .version(versionProvider.getVersion());

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
