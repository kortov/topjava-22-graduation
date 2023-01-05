package ru.kortov.topjava.graduation.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

//todo add description
//todo add security
public class OpenApiConfiguration {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                             .group("REST API")
                             .pathsToMatch("/api/**")
                             .packagesToScan("ru.kortov.topjava.graduation.web.controller")
                             .build();
    }
}
