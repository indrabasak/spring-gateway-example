package com.basaki.edge.filter.global;

import com.basaki.edge.security.SecurityAuthProperties;
import com.basaki.edge.util.Base64Encoder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import java.util.Arrays;
import java.util.List;

import static com.basaki.edge.filter.global.BasicAuthRelayGlobalFilter.BASIC_AUTH_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class BasicAuthRelayGlobalFilterTest {

    @Test
    public void testGetOrder() {
        SecurityAuthProperties properties = new SecurityAuthProperties();
        Base64Encoder encoder = new Base64Encoder();
        BasicAuthRelayGlobalFilter filter =
                new BasicAuthRelayGlobalFilter(properties, encoder);
        assertEquals(OrderConstant.FILTER_ORDER_AUTH_RELAY, filter.getOrder());
    }

    @Test
    @Parameters
    public void testFilter(String user, String password, boolean authHeaderExpected) {
        MockServerHttpRequest request =
                MockServerHttpRequest.get("http://localhost/get").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        String routeId = "my-route";
        Route route = mock(Route.class);
        when(route.getId()).thenReturn(routeId);
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, route);


        SecurityAuthProperties.Route routeSecurity = new SecurityAuthProperties.Route();
        routeSecurity.setUser(user);
        routeSecurity.setPassword(password);
        SecurityAuthProperties properties = new SecurityAuthProperties();
        properties.getRoutes().put(routeId, routeSecurity);

        Base64Encoder encoder = new Base64Encoder();
        BasicAuthRelayGlobalFilter filter =
                new BasicAuthRelayGlobalFilter(properties, encoder);

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        filter.filter(exchange, chain);

        if (authHeaderExpected) {
            assertTrue(request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
            List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
            assertNotNull(authHeaders);
            assertEquals(1, authHeaders.size());
            assertTrue(authHeaders.get(0).startsWith(BASIC_AUTH_PREFIX));
            assertEquals(encoder.encode(user + ":" + password),
                         authHeaders.get(0).substring(BASIC_AUTH_PREFIX.length()));
        } else {
            assertFalse(request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
        }
    }

    public Iterable<Object[]> parametersForTestFilter() {

        return Arrays.asList(new Object[][]{
                {"user01", "pwd01", true},
                {null, "pwd01", false},
                {"user01", null, false},
                {null, null, false},
        });
    }
}
