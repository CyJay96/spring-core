package ru.clevertec.ecl.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDtoRequest {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @JsonProperty("name")
    private String name;
}
