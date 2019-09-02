package com.basaki.edge.filter.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AddResponseTimeHeaderPostFilter
        extends AbstractGatewayFilterFactory<AddResponseTimeHeaderPostFilter.Config> {

    private static final String HEADER_TXN_DATE = "X-TXN-COMPLETION-DATE";

    public AddResponseTimeHeaderPostFilter() {
        super(AddResponseTimeHeaderPostFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String timestamp = LocalDateTime.now().toString();
            log.debug("Adding txn date header {}", timestamp);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> exchange.getResponse().getHeaders().add(HEADER_TXN_DATE, timestamp)));
        };
    }

    public static class Config {

    }
}
