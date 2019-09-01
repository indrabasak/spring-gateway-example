package com.basaki.edge.security;

public class BasicAuthenticator implements Authenticator<BasicAuthCredentials, BasicAuthExtractor> {
    @Override
    public BasicAuthExtractor getExtractor() {
        return new BasicAuthExtractor();
    }

    @Override
    public void authenticate(BasicAuthCredentials credentials) {

    }
}
