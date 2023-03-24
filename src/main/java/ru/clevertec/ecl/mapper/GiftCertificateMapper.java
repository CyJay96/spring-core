package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

@Mapper(componentModel = "spring")
public interface GiftCertificateMapper {

    GiftCertificate toEntity(GiftCertificateDtoRequest giftCertificateDtoRequest);

    @Mapping(target = "createDate", expression = "java(giftCertificate.getCreateDate() != null ? giftCertificate.getCreateDate().toString() : null)")
    @Mapping(target = "lastUpdateDate", expression = "java(giftCertificate.getCreateDate() != null ? giftCertificate.getCreateDate().toString() : null)")
    GiftCertificateDtoResponse toDtoResponse(GiftCertificate giftCertificate);
}
