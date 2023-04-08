package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag toEntity(TagDtoRequest tagDtoRequest);

    @Mapping(target = "giftCertificatesIds", expression = "java(tag.getGiftCertificates().stream().map(giftCertificate -> giftCertificate.getId()).toList())")
    TagDtoResponse toDto(Tag tag);
}
