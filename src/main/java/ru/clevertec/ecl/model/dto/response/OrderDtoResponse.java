package ru.clevertec.ecl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("giftCertificateId")
    private Long giftCertificateId;

    @JsonProperty("finalPrice")
    private BigDecimal finalPrice;

    @JsonProperty("createDate")
    private OffsetDateTime createDate;

    @JsonProperty("lastUpdateDate")
    private OffsetDateTime lastUpdateDate;
}
