package com.basaki.edge.config;

import com.basaki.edge.exception.BadConfigurationException;
import com.basaki.edge.security.Authenticator;
import com.basaki.edge.security.SecurityAuthProperties;
import com.basaki.edge.security.basic.BasicAuthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class SpringConfiguration {

    private SecurityAuthProperties properties;

    @Autowired
    public SpringConfiguration(SecurityAuthProperties properties) {
        this.properties = properties;
    }

    @Bean
    public BasicAuthProvider getBasicAuthProvider() {
        return new BasicAuthProvider(properties.getUser(), properties.getPassword());
    }

    @PostConstruct
    public void postConstruct() {
        properties.getRoutes().forEach((s, route) -> {
            for (Class clazz : route.getAuthenticatorClasses()) {
                if (!Authenticator.class.isAssignableFrom(clazz)) {
                    log.error("Route {} contains an invalid authenticator.", route.getId());
                    throw new BadConfigurationException("Invalid authenticator.");
                }
            }
        });
    }
}
