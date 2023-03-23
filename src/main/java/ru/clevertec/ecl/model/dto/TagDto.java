package ru.clevertec.ecl.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class TagDto {

    private Long id;

    private String name;
}
