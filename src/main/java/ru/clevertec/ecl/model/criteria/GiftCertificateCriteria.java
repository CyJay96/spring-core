package ru.clevertec.ecl.model.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.model.enums.SortType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateCriteria {

    @JsonProperty("tagName")
    private String tagName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("sortTypeName")
    private SortType sortTypeName;

    @JsonProperty("sortTypeDate")
    private SortType sortTypeDate;
}
