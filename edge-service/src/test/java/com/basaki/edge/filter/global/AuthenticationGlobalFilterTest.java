package com.basaki.edge.filter.global;

import com.basaki.edge.exception.AuthenticationException;
import com.basaki.edge.exception.BadCredentialsException;
import com.basaki.edge.security.Authenticator;
import com.basaki.edge.security.Credentials;
import com.basaki.edge.security.SecurityAuthProperties;
import com.basaki.edge.security.basic.BasicAuthCredentials;
import com.basaki.edge.security.basic.BasicAuthenticator;
import com.basaki.edge.security.oauth2.JwtAuthenticator;
import com.basaki.edge.security.oauth2.JwtCredentials;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import java.util.Arrays;

import static com.basaki.edge.filter.global.AuthenticationGlobalFilter.AUTH_CREDENTIALS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class AuthenticationGlobalFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetOrder() {
        SecurityAuthProperties properties = new SecurityAuthProperties();
        AuthenticationGlobalFilter filter = new AuthenticationGlobalFilter(properties);
        assertEquals(OrderConstant.FILTER_ORDER_AUTHENTICATION, filter.getOrder());
    }

    @Test
    @Parameters
    public void testFilter(Authenticator[] authenticators, boolean success,
                           Credentials credentials,
                           Class<? extends Exception> expectedException) {
        MockServerHttpRequest request =
                MockServerHttpRequest.get("http://localhost/get").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        String routeId = "my-route";
        Route route = mock(Route.class);
        when(route.getId()).thenReturn(routeId);
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, route);

        SecurityAuthProperties.Route routeSecurity = new SecurityAuthProperties.Route();
        routeSecurity.setAuthenticators(authenticators);

        SecurityAuthProperties properties = new SecurityAuthProperties();
        properties.getRoutes().put(routeId, routeSecurity);

        AuthenticationGlobalFilter filter = new AuthenticationGlobalFilter(properties);

        if (!success) {
            thrown.expect(expectedException);
        }

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        filter.filter(exchange, chain);

        assertNotNull(exchange.getAttribute(AUTH_CREDENTIALS));
        assertSame(credentials, exchange.getAttribute(AUTH_CREDENTIALS));
    }

    public Iterable<Object[]> parametersForTestFilter() {
        BasicAuthCredentials basicCredentials =
                BasicAuthCredentials.builder().user("user1").password("pwd0").build();

        BasicAuthenticator basicAtorSuccess = mock(BasicAuthenticator.class);
        when(basicAtorSuccess.authenticate(any())).thenReturn(basicCredentials);

        BasicAuthenticator basicAtorFailure = mock(BasicAuthenticator.class);
        when(basicAtorFailure.authenticate(any())).thenThrow(AuthenticationException.class);

        BasicAuthenticator basicAtorFailure2 = mock(BasicAuthenticator.class);
        when(basicAtorFailure2.authenticate(any())).thenThrow(BadCredentialsException.class);

        JwtCredentials jwtCredentials = new JwtCredentials();
        JwtAuthenticator jwtAtorSuccess = mock(JwtAuthenticator.class);
        when(jwtAtorSuccess.authenticate(any())).thenReturn(jwtCredentials);

        return Arrays.asList(new Object[][]{
                {new Authenticator[]{basicAtorSuccess}, true, basicCredentials, null},
                {new Authenticator[]{basicAtorFailure}, false, null, AuthenticationException.class},
                {new Authenticator[]{jwtAtorSuccess}, true, jwtCredentials, null},
                {new Authenticator[]{basicAtorFailure2, jwtAtorSuccess}, true, jwtCredentials, null},
        });
    }
}
