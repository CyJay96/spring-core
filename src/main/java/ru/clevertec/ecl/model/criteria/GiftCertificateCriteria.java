package ru.clevertec.ecl.model.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
public class GiftCertificateCriteria {

    @JsonProperty("tagNames")
    List<String> tagNames;

    @JsonProperty("description")
    String description;

    @JsonProperty("sortDirectionName")
    Sort.Direction sortDirectionName;

    @JsonProperty("sortDirectionDate")
    Sort.Direction sortDirectionDate;

    @JsonProperty("page")
    Integer offset;

    @JsonProperty("pageSize")
    Integer limit;
}
