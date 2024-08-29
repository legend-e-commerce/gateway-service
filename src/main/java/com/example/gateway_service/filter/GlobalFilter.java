package com.example.gateway_service.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        // 글로벌 Pre filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global PRE Filter Start baseMessage:" + config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Global PRE Filter Start: request id: " + request.getId());
            }

            // 글로벌 Post filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Global POST Filter End: response code: " + response.getStatusCode());
                }
            }));
        };
    }

    public GlobalFilter() {
        super(Config.class);
    }

    @Data
    public static class Config {
        // 해당 정보들은 yml 설정 파일에서 주입할 수 있다.
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
