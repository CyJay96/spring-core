package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag toEntity(TagDtoRequest tagDtoRequest);

    TagDtoResponse toDto(Tag tag);
}
