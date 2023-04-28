package ru.clevertec.ecl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value
@Builder
public class OrderDtoResponse {

    @JsonProperty("id")
    Long id;

    @JsonProperty("userId")
    Long userId;

    @JsonProperty("giftCertificateId")
    Long giftCertificateId;

    @JsonProperty("finalPrice")
    BigDecimal finalPrice;

    @JsonProperty("createDate")
    OffsetDateTime createDate;

    @JsonProperty("lastUpdateDate")
    OffsetDateTime lastUpdateDate;
}
