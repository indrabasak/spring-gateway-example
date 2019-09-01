package com.basaki.k8s.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * {@code DataConfiguration} configures an embedded database.
 * <p/>
 *
 * @author Indra Basak
 * @since 10/20/18
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.basaki.k8s.data.repository"})
@EnableTransactionManagement
public class DataConfiguration {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("db/create-db.sql")
                .build();
    }
}
