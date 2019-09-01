package com.basaki.edge.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("security.auth")
@Getter
@Setter
public class SecurityAuthProperties {

    private Map<String, Route> routes = new HashMap<>();

    @PostConstruct
    public void init() {
        routes.entrySet()
                .stream()
                .filter(e -> e.getValue().getId() == null)
                .forEach(e -> e.getValue().setId(e.getKey()));
    }

    @Data
    @Slf4j
    public static class Route {
        private String id;

        private String user;

        private String password;

        private Class<? extends Authenticator>[] authenticatorClasses;

        private Authenticator[] authenticators;
    }
}
