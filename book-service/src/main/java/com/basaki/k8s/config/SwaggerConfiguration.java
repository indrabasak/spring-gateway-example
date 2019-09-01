package com.basaki.k8s.config;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

/**
 * {@code SwaggerConfiguration} configures Swagger UI.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@Configuration
@EnableSwagger2
@SuppressWarnings({"squid:CallToDeprecatedMethod"})
public class SwaggerConfiguration {

    private static final String TITLE =
            "Spring Boot with Kubernetes and Docker";

    private static final String DESCRIPTION =
            "An example of using Spring Boot with Kubernetes and Docker";

    /**
     * Creates the Swagger Docket (configuration) bean.
     *
     * @return docket bean
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("book")
                .select()
                .apis(basePackage("com.basaki.k8s.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * Creates an object containing API information including version name,
     * license, etc.
     *
     * @return API information
     */
    private ApiInfo apiInfo() {
        Contact contact = new Contact("Indra Basak", "",
                "indra@basak.com");
        return new ApiInfo(TITLE, DESCRIPTION, "1.0.0",
                "terms of service url",
                contact, "license", "license url",
                new ArrayList<>());
    }
}
