package com.basaki.edge.filter.pre;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static com.basaki.edge.filter.pre.AddRequestTimeHeaderPreFilter.HEADER_TXN_DATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

public class AddRequestTimeHeaderPreFilterTest {

    @Test
    public void testApply() {
        String routeUri = "http://localhost/get";
        MockServerHttpRequest request =
                MockServerHttpRequest.get(routeUri).build();

        ServerWebExchange exchange = testFilter(request, routeUri);
        assertNotNull(exchange);
        assertNotNull(exchange.getRequest());
        assertTrue(request.getHeaders().containsKey(HEADER_TXN_DATE));
        List<String> headers = request.getHeaders().get(HEADER_TXN_DATE);
        assertNotNull(headers);
        assertEquals(1, headers.size());
    }

    private ServerWebExchange testFilter(MockServerHttpRequest request, String routeUri) {
        Route value = Route.async().id("1").uri(URI.create(routeUri)).order(0)
                .predicate(swe -> true).build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, value);

        GatewayFilterChain filterChain = mock(GatewayFilterChain.class);

        ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor
                .forClass(ServerWebExchange.class);
        when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());

        AddRequestTimeHeaderPreFilter.Config config =
                new AddRequestTimeHeaderPreFilter.Config();

        GatewayFilter filter = new AddRequestTimeHeaderPreFilter().apply(config);
        filter.filter(exchange, filterChain);

        return captor.getValue();
    }
}
