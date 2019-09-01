package com.basaki.edge.config;

import com.basaki.edge.exception.BadConfigurationException;
import com.basaki.edge.security.Authenticator;
import com.basaki.edge.security.SecurityAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoutePostProcessor implements SmartLifecycle {

    private ApplicationContext context;

    private SecurityAuthProperties properties;

    @Autowired
    public RoutePostProcessor(ApplicationContext context, SecurityAuthProperties properties) {
        this.context = context;
        this.properties = properties;
    }

    @Override
    public void start() {
        properties.getRoutes().forEach((id, route) -> {
            log.debug("Processing route {}", id);
            Class[] clazzes = route.getAuthenticatorClasses();
            Authenticator[] authenticators = new Authenticator[clazzes.length];

            for (int i = 0; i < clazzes.length; i++) {
                Authenticator bean;
                try {
                    bean = (Authenticator) context.getAutowireCapableBeanFactory().createBean(clazzes[i]);
                } catch (BeansException e) {
                    throw new BadConfigurationException(
                            "Could not find bean of requested type for route " + id);
                }

                if (bean == null) {
                    log.error("Invalid configuration of route {}.", route.getId());
                    throw new BadConfigurationException(
                            "Invalid configuration. Authentication must be an instance of Authenticator.");
                }

                authenticators[i] = bean;
            }

            route.setAuthenticators(authenticators);
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
