package com.basaki.edge.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Base64;

public class BasicAuthExtractor implements AuthenticationExtractor<BasicAuthCredentials> {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String PREFIX_BASIC = "Basic ";

    public BasicAuthCredentials extract(ServerHttpRequest request) {
        String authHeader = request.getHeaders().get(HEADER_AUTHORIZATION).get(0);

        if (StringUtils.isEmpty(authHeader)) {
            throw new RuntimeException("No Credentials Provided");
        }

        if (!authHeader.startsWith(PREFIX_BASIC)) {
            throw new RuntimeException("Invalid Authorization header provided");
        }

        String[] authSegments = decode(authHeader.substring(PREFIX_BASIC.length())).split(":");

        System.out.println("user: " + authSegments[0]);
        System.out.println("password: " + authSegments[1]);

        return BasicAuthCredentials.builder().user(authSegments[0]).password(authSegments[1]).build();
    }

    public String decode(String toDecode) {
        return new String(Base64.getDecoder().decode(toDecode));
    }
}
