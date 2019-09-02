package com.basaki.edge.security.basic;

import com.basaki.edge.exception.BadCredentialsException;
import com.basaki.edge.security.AuthenticationExtractor;
import com.basaki.edge.util.Base64Encoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthExtractor implements AuthenticationExtractor<BasicAuthCredentials> {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String PREFIX_BASIC = "Basic ";

    private Base64Encoder encoder;

    @Autowired
    public BasicAuthExtractor(Base64Encoder encoder) {
        this.encoder = encoder;
    }

    public BasicAuthCredentials extract(ServerHttpRequest request) {
        String authHeader = request.getHeaders().get(HEADER_AUTHORIZATION).get(0);

        if (StringUtils.isEmpty(authHeader)) {
            throw new BadCredentialsException("No Credentials Provided");
        }

        if (!authHeader.startsWith(PREFIX_BASIC)) {
            throw new BadCredentialsException("Invalid Authorization header provided");
        }

        String[] authSegments = encoder.decode(authHeader.substring(PREFIX_BASIC.length())).split(":");

        System.out.println("user: " + authSegments[0]);
        System.out.println("password: " + authSegments[1]);

        return BasicAuthCredentials.builder().user(authSegments[0]).password(authSegments[1]).build();
    }
}
