package com.basaki.edge.security.basic;

import com.basaki.edge.security.Credentials;
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
