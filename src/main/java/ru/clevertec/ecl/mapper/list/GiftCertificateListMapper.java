package ru.clevertec.ecl.mapper.list;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.List;

@Mapper(componentModel = "spring", uses = GiftCertificateMapper.class)
public interface GiftCertificateListMapper {

    List<GiftCertificate> toEntity(List<GiftCertificateDtoRequest> giftCertificateDtoRequestList);

    List<GiftCertificateDtoResponse> toDtoResponse(List<GiftCertificate> giftCertificateList);
}
