package ru.clevertec.ecl.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class UserDtoResponse {

    @JsonProperty("id")
    Long id;

    @JsonProperty("username")
    String username;

    @JsonProperty("firstName")
    String firstName;

    @JsonProperty("lastName")
    String lastName;

    @JsonProperty("email")
    String email;

    @JsonProperty("status")
    String status;

    @JsonProperty("createDate")
    OffsetDateTime createDate;

    @JsonProperty("lastUpdateDate")
    OffsetDateTime lastUpdateDate;

    @JsonProperty("ordersIds")
    List<Long> ordersIds;
}
