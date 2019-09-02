package com.basaki.edge.security;

import com.basaki.edge.exception.AuthenticationException;
import com.basaki.edge.exception.BadCredentialsException;
import org.springframework.http.server.reactive.ServerHttpRequest;

public interface Authenticator<C extends Credentials, A extends AuthenticationExtractor<C>> {

    A getExtractor();

    C extract(ServerHttpRequest request) throws BadCredentialsException;

    void authenticate(C credentials) throws AuthenticationException;
}
