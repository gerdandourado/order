package br.com.avaliacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    @Value("${server.port}")
    private Integer port;

    @Test
    void dadoPortaValida_quandoGetConfigOpenApi_entaoRetornaOpenAPIConfigurado() {
        Integer portaMock = 8080;
        OpenApiConfig openApiConfig = new OpenApiConfig(portaMock);
        OpenAPI openAPI = openApiConfig.getConfigOpenApi();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getServers());
        assertEquals(1, openAPI.getServers().size());
        Server server = openAPI.getServers().get(0);
        assertEquals("http://localhost:8080/order/", server.getUrl());

        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("Ambev API", info.getTitle());
        assertEquals("Ambev API Documentation", info.getDescription());
        assertEquals("v0.0.1", info.getVersion());
        
        License license = info.getLicense();
        assertNotNull(license);
        assertEquals("Apache 2.0", license.getName());
    }
}