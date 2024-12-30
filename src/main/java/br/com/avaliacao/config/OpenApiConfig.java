package br.com.avaliacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    private final Integer port;
    private static final String URI_PROJECT = "/order/";

    @Autowired
    public OpenApiConfig(@Value("${server.port}") Integer port) {
        this.port = port;
    }

    @Bean
    public OpenAPI getConfigOpenApi() {
        List<Server> servers = List.of((new Server()).url("http://localhost:" + port + URI_PROJECT));

        return new OpenAPI()
                .servers(servers)
                .info(
                        new Info()
                                .title("Ambev API")
                                .description("Ambev API Demonstration")
                                .version("v0.0.1")
                                .license(new License().name("Apache 2.0"))
                                .description("Ambev API Documentation")
                );
    }
}