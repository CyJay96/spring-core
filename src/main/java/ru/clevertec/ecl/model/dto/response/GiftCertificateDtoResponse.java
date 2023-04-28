package ru.clevertec.ecl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class GiftCertificateDtoResponse {

    @JsonProperty("id")
    Long id;

    @JsonProperty("name")
    String name;

    @JsonProperty("description")
    String description;

    @JsonProperty("price")
    BigDecimal price;

    @JsonProperty("duration")
    Long duration;

    @JsonProperty("createDate")
    OffsetDateTime createDate;

    @JsonProperty("lastUpdateDate")
    OffsetDateTime lastUpdateDate;

    @JsonProperty("tags")
    List<TagDtoResponse> tags;
}
