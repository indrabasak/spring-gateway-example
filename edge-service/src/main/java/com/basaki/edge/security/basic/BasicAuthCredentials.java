package com.basaki.edge.security.basic;

import com.basaki.edge.security.Credentials;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicAuthCredentials implements Credentials {
    private String user;

    private String password;

    private boolean authenticated;
}
