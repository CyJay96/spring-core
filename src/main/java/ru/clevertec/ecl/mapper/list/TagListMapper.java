package ru.clevertec.ecl.mapper.list;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TagListMapper {

    List<Tag> toEntity(List<TagDtoRequest> tagDtoRequestList);

    List<TagDtoResponse> toDto(List<Tag> tagList);
}
