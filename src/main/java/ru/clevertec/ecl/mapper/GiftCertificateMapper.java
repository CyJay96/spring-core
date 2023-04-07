package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface GiftCertificateMapper {

    @Mapping(target = "duration", expression = "java(java.time.Duration.ofDays(giftCertificateDtoRequest.getDuration()))")
    GiftCertificate toEntity(GiftCertificateDtoRequest giftCertificateDtoRequest);

    @Mapping(target = "duration", expression = "java(giftCertificate.getDuration().toDays())")
    @Mapping(target = "createDate", expression = "java(giftCertificate.getCreateDate() != null ? giftCertificate.getCreateDate().toString() : null)")
    @Mapping(target = "lastUpdateDate", expression = "java(giftCertificate.getCreateDate() != null ? giftCertificate.getCreateDate().toString() : null)")
    GiftCertificateDtoResponse toDto(GiftCertificate giftCertificate);
}
