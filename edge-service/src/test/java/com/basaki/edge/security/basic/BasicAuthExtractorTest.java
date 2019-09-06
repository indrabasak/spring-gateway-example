package com.basaki.edge.security.basic;

import com.basaki.edge.exception.BadCredentialsException;
import com.basaki.edge.util.Base64Encoder;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class BasicAuthExtractorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Base64Encoder encoder = new Base64Encoder();

    @Test
    @Parameters
    public void testExtract(ServerHttpRequest request,
                            String user, String password,
                            Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            thrown.expect(expectedException);
        }

        Base64Encoder encoder = new Base64Encoder();
        BasicAuthExtractor extractor = new BasicAuthExtractor(encoder);

        BasicAuthCredentials credentials = extractor.extract(request);
        assertNotNull(credentials);
        assertEquals(user, credentials.getUser());
        assertEquals(password, credentials.getPassword());
    }

    public Iterable<Object[]> parametersForTestExtract() {
        return Arrays.asList(new Object[][]{
                {getRequest(null, null), null, null,
                        BadCredentialsException.class},
                {getRequest("Authorization", ""), null, null,
                        BadCredentialsException.class},
                {getRequest("Authorization", "NotBasic"), null, null,
                        BadCredentialsException.class},
                {getRequest("Authorization", "Basic " + encoder.encode("user:password")),
                        "user", "password", null},
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
