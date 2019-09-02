package com.basaki.edge.security.basic;

import com.basaki.edge.security.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class BasicAuthenticator implements Authenticator<BasicAuthCredentials> {

    private BasicAuthProvider provider;

    private BasicAuthExtractor extractor;

    @Autowired
    public BasicAuthenticator(BasicAuthExtractor extractor, BasicAuthProvider provider) {
        this.extractor = extractor;
        this.provider = provider;
    }

    @Override
    public BasicAuthCredentials authenticate(ServerHttpRequest request) {
        BasicAuthCredentials credentials = extractor.extract(request);
        provider.authenticate(credentials.getUser(), credentials.getPassword());

        return credentials;
    }
}
