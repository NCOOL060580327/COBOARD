package com.example.demo.global.config;

import java.lang.reflect.Method;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.global.annotation.SwaggerDocs;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI coBoardAPI() {
    Info info = new Info().title("CoBoard API").description("CoBoard API 명세").version("0.0.1");

    String jwtSchemeName = "JWT";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

    Components components =
        new Components()
            .addSecuritySchemes(
                jwtSchemeName,
                new SecurityScheme()
                    .name(jwtSchemeName)
                    .type(Type.HTTP)
                    .scheme("Bearer")
                    .bearerFormat("JWT"));

    return new OpenAPI()
        .addServersItem(new Server().url("/"))
        .info(info)
        .addSecurityItem(securityRequirement)
        .components(components);
  }

  @Bean
  public OperationCustomizer customize() {
    return (operation, handlerMethod) -> {
      SwaggerDocs useSwaggerDocs = handlerMethod.getMethodAnnotation(SwaggerDocs.class);
      if (useSwaggerDocs != null) {
        try {
          Class<?> swaggerDocsClass = useSwaggerDocs.value();
          Method swaggerMethod =
              swaggerDocsClass.getDeclaredMethod(handlerMethod.getMethod().getName());
          io.swagger.v3.oas.annotations.Operation operationFromDocs =
              swaggerMethod.getAnnotation(io.swagger.v3.oas.annotations.Operation.class);
          ApiResponses apiResponsesFromDocs = swaggerMethod.getAnnotation(ApiResponses.class);

          if (operationFromDocs != null) {
            operation.setSummary(operationFromDocs.summary());
            operation.setDescription(operationFromDocs.description());
          }

          if (apiResponsesFromDocs != null) {
            io.swagger.v3.oas.models.responses.ApiResponses responses =
                new io.swagger.v3.oas.models.responses.ApiResponses();
            for (io.swagger.v3.oas.annotations.responses.ApiResponse response :
                apiResponsesFromDocs.value()) {
              ApiResponse swaggerResponse = new ApiResponse().description(response.description());

              if (response.content() != null && response.content().length > 0) {
                Content content = new Content();
                for (io.swagger.v3.oas.annotations.media.Content cont : response.content()) {
                  MediaType mediaType = new MediaType();
                  if (cont.examples() != null && cont.examples().length > 0) {
                    for (io.swagger.v3.oas.annotations.media.ExampleObject example :
                        cont.examples()) {
                      mediaType.addExamples(
                          example.name(),
                          new io.swagger.v3.oas.models.examples.Example()
                              .description(example.description())
                              .value(example.value()));
                    }
                  }
                  content.addMediaType(cont.mediaType(), mediaType);
                }
                swaggerResponse.setContent(content);
              }

              responses.addApiResponse(response.responseCode(), swaggerResponse);
            }
            operation.setResponses(responses);
          }
        } catch (NoSuchMethodException e) {
          throw new RuntimeException("Swagger 정의 클래스에서 메서드를 찾을 수 없습니다.", e);
        }
      }
      return operation;
    };
  }
}
