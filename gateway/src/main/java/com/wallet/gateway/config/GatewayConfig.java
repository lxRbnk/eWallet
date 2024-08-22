package com.wallet.gateway.config;

import com.wallet.gateway.filter.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GatewayConfig {

    private JwtFilter jwtFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:8081"))
                .route("user_service", r -> r
                        .path("/user/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://localhost:8082"))
                .route("wallet_service", r -> r
                        .path("/wallet/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://localhost:8083"))
                .route("report_service", r -> r
                        .path("/report/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://localhost:8084"))
                .build();
    }
}