package ru.kortov.topjava.graduation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@SecurityScheme(
    name = "basicAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "basic"
)
@OpenAPIDefinition(
    info = @Info(
        title = "REST API documentation",
        version = "1.0",
        description = """
                      <p><b>Тестовые креденшелы:</b><br>
                      - user@yandex.ru / password<br>
                      - admin@gmail.com / admin<br>
                      - guest@gmail.com / guest</p>
                      """,
        contact = @Contact(name = "Eugene Kortov")
    ),
    security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfiguration {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                             .group("REST API")
                             .pathsToMatch("/api/**")
                             .packagesToScan("ru.kortov.topjava.graduation.web.controller")
                             .build();
    }

    static {
        // https://stackoverflow.com/q/70799677/9466638
        var schema = new Schema<LocalTime>();
        schema.example(LocalTime.now().format(DateTimeFormatter.ISO_TIME));
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, schema);
    }
}
