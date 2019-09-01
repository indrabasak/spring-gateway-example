package com.basaki.k8s.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * {@code SpringConfiguration} creates bean for logging request.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@Configuration
public class SpringConfiguration implements WebMvcConfigurer {

    @Primary
    @Bean(name = "customObjectMapper")
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter standardConverter =
                new MappingJackson2HttpMessageConverter();
        standardConverter.setPrefixJson(false);
        standardConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_PLAIN));
        standardConverter.setObjectMapper(createObjectMapper());
        return standardConverter;
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}
