package com.basaki.edge.security.oauth2;

import com.basaki.edge.exception.BadCredentialsException;
import com.basaki.edge.security.AuthenticationExtractor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwtExtractor implements AuthenticationExtractor<String> {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$");

    @Override
    public String extract(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (headers == null || headers.isEmpty()) {
            throw new BadCredentialsException("Authorization header is missing!");
        }

        String authorization = headers.get(0);

        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            Matcher matcher = authorizationPattern.matcher(authorization);

            if (!matcher.matches()) {
                throw new BadCredentialsException("Bearer token is malformed!");
            }
            return matcher.group("token");
        }

        throw new BadCredentialsException("Invalid Authorization header provided!");
    }
}
