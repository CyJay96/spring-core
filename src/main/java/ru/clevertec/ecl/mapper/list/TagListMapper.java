package ru.clevertec.ecl.mapper.list;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface TagListMapper {

    List<Tag> toEntity(List<TagDto> tagDtoList);

    List<TagDto> toDto(List<Tag> tagList);
}
