package com.basaki.edge.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BasicAuthCredentials implements Credentials {
    private String user;

    private String password;

    private boolean authenticated;
}
