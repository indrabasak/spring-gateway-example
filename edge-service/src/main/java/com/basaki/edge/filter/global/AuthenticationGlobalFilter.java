package com.basaki.edge.filter.global;

import com.basaki.edge.security.BasicAuthCredentials;
import com.basaki.edge.security.BasicAuthExtractor;
import com.basaki.edge.security.SecurityAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private SecurityAuthProperties properties;

    private BasicAuthExtractor extractor;

    @Autowired
    public AuthenticationGlobalFilter(SecurityAuthProperties properties) {
        this.properties = properties;
        extractor = new BasicAuthExtractor();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

        if (route != null) {
            log.info("AuthenticationGlobalFilter - start");
            SecurityAuthProperties.Route routeSecurity = properties.getRoutes().get(route.getId());
            System.out.println(routeSecurity.getUser() + " - " + routeSecurity.getPassword());
            BasicAuthCredentials info = extractor.extract(exchange.getRequest());
            exchange.getAttributes().put("AUTH_INFO", info);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
