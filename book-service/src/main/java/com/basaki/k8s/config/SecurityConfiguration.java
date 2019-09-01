package com.basaki.k8s.config;

import com.basaki.k8s.error.exception.SecurityConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * {@code SecurityConfiguration} is the base Spring security
 * configuration for Eureka server. It can handle multiple users.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityAuthProperties.class)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final SecurityAuthProperties properties;

    @Autowired
    public SecurityConfiguration(SecurityAuthProperties properties) {
        this.properties = properties;
    }

    /**
     * Configures HTTP security access control by restricting endpoints based on
     * roles. The restricted service id are declared as security.auth.*
     * properties.
     * <pre>
     * security:
     *   auth:
     *     endpoints:
     *       endpoint1:
     *         path: /books
     *         methods: POST
     *         roles: BOOK_WRITE
     *       endpoint2:
     *         path: /books/**
     *         methods: GET
     *         roles: BOOK_WRITE, BOOK_READ
     * </pre>
     *
     * @param http the HTTP security object to be configured.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        properties.getEndpoints().forEach((key, value) -> {
            try {
                for (HttpMethod method : value.getMethods()) {
                    http.authorizeRequests()
                            .antMatchers(method, value.getPath())
                            .hasAnyAuthority(value.getRoles())
                            .and().httpBasic().and().csrf().disable();
                    log.info("Adding security for path " + value.getPath()
                            + " and method " + method);
                }
            } catch (Exception e) {
                throw new SecurityConfigurationException(
                        "Problem encountered while setting up " +
                                "endpoint restrictions", e);
            }
        });

        http.sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS);
    }

    /**
     * Configures the security provider with an in-memory authenticator manager.
     *
     * @param auth builder for creating the security manager
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws
            Exception {
        properties.getUsers().forEach((key, value) -> {
            try {
                log.info("Added user " + value.getId() + " with password "
                        + value.getPassword());

                auth.inMemoryAuthentication()
                        .passwordEncoder(
                                PasswordEncoderFactories.createDelegatingPasswordEncoder())
                        .withUser(value.getId())
                        .password(value.getPassword())
                        .roles(value.getRoles());
            } catch (Exception e) {
                throw new SecurityConfigurationException(
                        "Problem encountered while setting up " +
                                "authentication mananger", e);
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs/**");
        web.ignoring().antMatchers("/swagger.json");
        web.ignoring().antMatchers("/swagger-ui.html");
        web.ignoring().antMatchers("/swagger-resources/**");
        web.ignoring().antMatchers("/webjars/**");
    }
}
