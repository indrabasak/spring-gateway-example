package com.basaki.edge.security.basic;

import com.basaki.edge.security.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class BasicAuthenticator implements Authenticator<BasicAuthCredentials, BasicAuthExtractor> {

    private BasicAuthProvider provider;

    private BasicAuthExtractor extractor;

    @Autowired
    public BasicAuthenticator(BasicAuthExtractor extractor, BasicAuthProvider provider) {
        this.extractor = extractor;
        this.provider = provider;
    }

    @Override
    public BasicAuthExtractor getExtractor() {
        return extractor;
    }

    @Override
    public BasicAuthCredentials extract(ServerHttpRequest request) {
        return extractor.extract(request);
    }

    @Override
    public void authenticate(BasicAuthCredentials credentials) {
        provider.authenticate(credentials.getUser(), credentials.getPassword());
    }
}
