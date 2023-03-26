package ru.clevertec.ecl.model.criteria;

import lombok.Builder;
import lombok.Getter;
import ru.clevertec.ecl.model.enums.SortType;

@Getter
@Builder
public class GiftCertificateCriteria {

    private final String tagName;

    private final String description;

    private final SortType sortTypeName;

    private final SortType sortTypeDate;
}
