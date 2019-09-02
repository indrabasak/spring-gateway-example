package com.basaki.edge.security.oauth2;

import com.basaki.edge.security.Credentials;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.jwt.Jwt;

@Getter
@Setter
public class JwtCredentials implements Credentials {

    private Jwt jwt;
}
