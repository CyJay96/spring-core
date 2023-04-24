package ru.clevertec.ecl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDtoResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("duration")
    private Long duration;

    @JsonProperty("createDate")
    private OffsetDateTime createDate;

    @JsonProperty("lastUpdateDate")
    private OffsetDateTime lastUpdateDate;

    @JsonProperty("tags")
    private List<TagDtoResponse> tags;
}
