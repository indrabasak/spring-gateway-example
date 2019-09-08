package com.basaki.edge;

import com.basaki.edge.security.SecurityAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.basaki.edge"}, exclude = {GatewayAutoConfiguration.class})
@EnableConfigurationProperties({SecurityAuthProperties.class})
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
