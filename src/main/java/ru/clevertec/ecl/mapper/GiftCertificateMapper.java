package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

@Mapper(componentModel = "spring")
public interface GiftCertificateMapper {

    GiftCertificate toEntity(GiftCertificateDtoRequest giftCertificateDtoRequest);

    GiftCertificateDtoResponse toDtoResponse(GiftCertificate giftCertificate);
}
