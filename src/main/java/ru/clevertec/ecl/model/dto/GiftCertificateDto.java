package ru.clevertec.ecl.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class GiftCertificateDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Long duration;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime createDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime lastUpdateDate;

    private List<TagDto> tags;
}
