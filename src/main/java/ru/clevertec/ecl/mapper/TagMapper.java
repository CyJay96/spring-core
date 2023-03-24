package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.model.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag toEntity(TagDto tagDto);

    TagDto toDto(Tag tag);
}
