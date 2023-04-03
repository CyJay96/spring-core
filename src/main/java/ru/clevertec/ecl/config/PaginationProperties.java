package ru.clevertec.ecl.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PaginationProperties {

    private final Integer defaultPageValue = 0;

    private final Integer defaultPageSize = 18;
}
