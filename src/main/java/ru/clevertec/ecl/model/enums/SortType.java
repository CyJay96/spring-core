package ru.clevertec.ecl.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SortType {

    @JsonProperty("asc")
    ASC,

    @JsonProperty("desc")
    DESC
}
