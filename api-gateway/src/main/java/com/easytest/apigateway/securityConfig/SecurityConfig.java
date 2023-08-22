package com.easytest.apigateway.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){

        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeExchange(authorize ->
                        authorize.pathMatchers("/eureka/**").permitAll()
                                .pathMatchers("/api/v1/quiz-session/participant/**").hasAnyRole("USER, ADMIN")
                                .pathMatchers("/api/v1/quiz-session/examiner/**").hasRole("ADMIN")
                                .pathMatchers("/api/v1/question/**").hasRole("ADMIN")
                                .anyExchange().authenticated()
                ).oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtAuthConverter)));
        return httpSecurity.build();


    }
}
