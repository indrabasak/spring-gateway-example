package com.basaki.edge.security.oauth2;

import com.basaki.edge.exception.BadCredentialsException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class JwtExtractorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Parameters
    public void testExtract(ServerHttpRequest request,
                            Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            thrown.expect(expectedException);
        }

        JwtExtractor extractor = new JwtExtractor();

        String credentials = extractor.extract(request);
        assertNotNull(credentials);
    }

    public Iterable<Object[]> parametersForTestExtract() {
        return Arrays.asList(new Object[][]{
                {getRequest(null, null), BadCredentialsException.class},
                {getRequest("Authorization", ""), BadCredentialsException.class},
                {getRequest("Authorization", null), BadCredentialsException.class},
                {getRequest("Authorization", "NotBearer"), BadCredentialsException.class},
                {getRequest("Authorization", "Bearer abcd!@#$"), BadCredentialsException.class},
                {getRequest("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlEwWXlRVGMyTWpGRVJEQXhOa0ZHTVRGQlJqZzJNRVZGTUVFd01UZENOREV6TmpJd1JqTTFOQSJ9.eyJpc3MiOiJodHRwczovL2liYXNhay5hdXRoMC5jb20vIiwic3ViIjoieUhKaUplY0xuM2JkOEEyb3VtbVJhMDhqcDl0MHkxVUxAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vcXVpY2tzdGFydHMvYXBpIiwiaWF0IjoxNTY3Nzg5MDQ2LCJleHAiOjE1Njc4NzU0NDYsImF6cCI6InlISmlKZWNMbjNiZDhBMm91bW1SYTA4anA5dDB5MVVMIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.aFzEvDwsNvUge5yAkzLJfrlpjtxffO2M7V0q0sGF9udi99KVEK3vQ2KXZm_N7v-ASrm-LF7twgPzdiln6tVMWkGtvFmpKx2YQwmXsEDYZGfrHOwb5XjY2AF8eXXsiJQEyI_SOSb-CzoAxFL34eIPeFa77zR6nmcIZAJyCdTtrMd1S4XIENPW1aWvwK5BVqFk6VpJ33LdemQYthQkNMYJF_v8dgXHbqSIAkdOfg4CUKXRObABTc4LnARMiFGFa-c2aQBMj1vP6PRE7h41Fr6MTHkUSVfFFayyVUFI3mH3tfiNHTqQiUZIpNJNknRYCTXDJq2V4mLgWfH9BFjelP65dg"),
                        null},
        });
    }

    private ServerHttpRequest getRequest(String key, String value) {
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        HttpHeaders headers = new HttpHeaders();
        when(request.getHeaders()).thenReturn(headers);

        if (key != null && value != null) {
            List<String> values = new ArrayList<>();
            values.add(value);
            headers.put(key, values);
        }

        return request;
    }
}
