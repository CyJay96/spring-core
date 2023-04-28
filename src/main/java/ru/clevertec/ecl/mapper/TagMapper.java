package ru.clevertec.ecl.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;

@Mapper
public interface TagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "giftCertificates", ignore = true)
    Tag toTag(TagDtoRequest tagDtoRequest);

    @Mapping(
            target = "giftCertificatesIds",
            expression = "java(java.util.Objects.nonNull(tag.getGiftCertificates()) ? " +
                    "tag.getGiftCertificates().stream().map(giftCertificate -> giftCertificate.getId()).toList() : " +
                    "new ArrayList<>())"
    )
    TagDtoResponse toTagDtoResponse(Tag tag);

    List<Tag> toTagList(List<TagDtoRequest> tagDtoRequestList);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "giftCertificates", ignore = true)
    void updateTag(TagDtoRequest tagDtoRequest, @MappingTarget Tag tag);
}
