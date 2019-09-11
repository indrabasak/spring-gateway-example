package com.basaki.edge.security.basic;

import com.basaki.edge.exception.BadCredentialsException;
import com.basaki.edge.security.AuthenticationExtractor;
import com.basaki.edge.util.Base64Encoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicAuthExtractor implements AuthenticationExtractor<BasicAuthCredentials> {

    public static final String PREFIX_BASIC = "Basic ";

    private Base64Encoder encoder;

    @Autowired
    public BasicAuthExtractor(Base64Encoder encoder) {
        this.encoder = encoder;
    }

    public BasicAuthCredentials extract(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (headers == null || headers.isEmpty()) {
            throw new BadCredentialsException("Authorization header is missing!");
        }

        String authorization = headers.get(0);

        if (StringUtils.isEmpty(authorization)) {
            throw new BadCredentialsException("No Credentials Provided");
        }

        if (!authorization.startsWith(PREFIX_BASIC)) {
            throw new BadCredentialsException("Invalid Authorization header provided");
        }

        String[] authSegments = encoder.decode(authorization.substring(PREFIX_BASIC.length())).split(":");

        BasicAuthCredentials credentials = new BasicAuthCredentials();
        credentials.setUser(authSegments[0]);
        credentials.setPassword(authSegments[1]);

        return credentials;
    }
}
