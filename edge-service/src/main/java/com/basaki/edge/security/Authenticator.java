package com.basaki.edge.security;

public interface Authenticator<C extends Credentials, A extends AuthenticationExtractor<C>> {

    A getExtractor();

    void authenticate(C credentials);
}
