package com.clinica.agendamento.config;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configuração global de CORS para permitir que o frontend (Angular) acesse a API (Spring Boot) sem problemas de bloqueio de 
// origem cruzada.
// O front-end Angular roda em http://localhost:4200 e a API em http://localhost:8080.
// Como navegadores bloqueiam requisições de origens diferentes por padrão, liberamos explicitamente o acesso do front-end à API.

@Configuration
public class CorsConfig implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
 