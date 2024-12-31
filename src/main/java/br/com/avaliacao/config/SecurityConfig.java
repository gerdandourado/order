package br.com.avaliacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui.html", // Página principal do Swagger UI
                                "/v3/api-docs/**",  // OpenAPI JSON/YAML
                                "/swagger-ui/**",   // Recursos estáticos (CSS, JS)
                                "/webjars/**"             // Dependências de terceiros do Swagger UI
                        ).permitAll() // Permitir acesso público a esses paths
                        .requestMatchers("/pedidos/**").authenticated() // Requer autenticação para "/order/pedidos/**"
                        .anyRequest().authenticated() // Autenticação para qualquer outra requisição
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
