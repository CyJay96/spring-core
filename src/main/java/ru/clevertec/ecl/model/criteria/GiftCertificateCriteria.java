package ru.clevertec.ecl.model.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateCriteria {

    @JsonProperty("tagNames")
    private List<String> tagNames;

    @JsonProperty("description")
    private String description;

    @JsonProperty("sortDirectionName")
    private Sort.Direction sortDirectionName;

    @JsonProperty("sortDirectionDate")
    private Sort.Direction sortDirectionDate;

    @JsonProperty("page")
    private Integer offset;

    @JsonProperty("pageSize")
    private Integer limit;
}
