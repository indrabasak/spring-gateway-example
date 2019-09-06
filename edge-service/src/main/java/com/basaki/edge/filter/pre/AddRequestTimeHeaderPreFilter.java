package com.basaki.edge.filter.pre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@SuppressWarnings({"squid:S2094"})
public class AddRequestTimeHeaderPreFilter extends AbstractGatewayFilterFactory<AddRequestTimeHeaderPreFilter.Config> {

    public static final String HEADER_TXN_DATE = "X-TXN-DATE";

    public AddRequestTimeHeaderPreFilter() {
        super(AddRequestTimeHeaderPreFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(AddRequestTimeHeaderPreFilter.Config config) {
        return (exchange, chain) -> {
            String timestamp = LocalDateTime.now().toString();

            log.debug("Adding txn date header {}", timestamp);
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .headers(httpHeaders -> httpHeaders.set(HEADER_TXN_DATE, timestamp))
                    .build();

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {

    }
}
