package com.basaki.edge.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface Authenticator<C extends Credentials, A extends AuthenticationExtractor<C>> {

    A getExtractor();

    C extract(ServerHttpRequest request);

    void authenticate(C credentials);
}
