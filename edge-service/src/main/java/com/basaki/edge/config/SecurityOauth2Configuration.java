package com.basaki.edge.config;

import com.basaki.edge.security.oauth2.AudienceValidator;
import com.basaki.edge.security.oauth2.JwtExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;

@Configuration
@ConditionalOnProperty(name = {"spring.security.oauth2.resourceserver.jwt.issuer-uri"})
public class SecurityOauth2Configuration {

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:}")
    private String issuer;

    @Bean(name = "customJwtDecoder")
    public JwtDecoder getJwtDecoder() {
        NimbusJwtDecoderJwkSupport jwtDecoder = (NimbusJwtDecoderJwkSupport)
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean
    public JwtExtractor getJwtExtractor() {
        return new JwtExtractor();
    }
}
