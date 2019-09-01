package com.basaki.k8s.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

/**
 * {@code SecurityAuthProperties} represents the basic authorization security
 * properties. The properties are declared as security.auth.*. .
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
 *     users:
 *       userA:
 *         encoding: bcrypt
 *         password: pwdA
 *         roles: BOOK_WRITE, BOOK_READ
 *       userB:
 *         encoding: bcrypt
 *         password: pwdB
 *         roles: BOOK_READ
 * </pre>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@ConfigurationProperties("security.auth")
@Getter
@Setter
public class SecurityAuthProperties {

    private static final String ROLE_PREFIX = "ROLE_";

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    private Map<String, Endpoint> endpoints = new HashMap<>();

    private Map<String, User> users = new HashMap<>();

    @PostConstruct
    public void init() {
        endpoints.forEach((key, value) -> {
            List<String> roles = new ArrayList<>();
            for (String role : value.getRoles()) {
                roles.add(ROLE_PREFIX + role);
            }
            value.setRoles(roles.toArray(new String[0]));
        });

        users.forEach((key, value) -> {
            if (value.getId() == null) {
                value.setId(key);
            }

            if (value.getEncoding() != null) {
                value.setPassword(
                        "{" + value.getEncoding().trim() + "}"
                                + value.getPassword());
            } else {
                value.setPassword("{noop}" + value.getPassword());
            }
        });
    }

    @Getter
    @Setter
    public static class Endpoint {

        private String path;

        private HttpMethod[] methods;

        private String[] roles;

        public String[] getRoles() {
            if (roles == null || roles.length == 0) {
                roles = new String[1];
                roles[0] = ROLE_ANONYMOUS;
            }

            return roles;
        }

    }

    /**
     * {@code User} represents a basic auth security user with an id and
     * password.
     * <p/>
     *
     * @author Indra Basak
     */
    @Getter
    @Setter
    public static class User {

        private String id;

        private String encoding;

        private String password;

        private String[] roles;

        public String[] getRoles() {
            if (roles == null || roles.length == 0) {
                roles = new String[1];
                roles[0] = ROLE_ANONYMOUS;
            }

            return roles;
        }
    }
}
