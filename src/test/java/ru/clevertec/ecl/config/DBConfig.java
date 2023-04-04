package ru.clevertec.ecl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static ru.clevertec.ecl.util.TestConstants.CREATE_DB;
import static ru.clevertec.ecl.util.TestConstants.INIT_DB;

@Configuration
@Profile("dev")
@ComponentScan(value = "ru.clevertec.ecl")
public class DBConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(CREATE_DB)
                .addScript(INIT_DB)
                .build();
    }
}
