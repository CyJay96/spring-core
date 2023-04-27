package ru.clevertec.ecl.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

@Mapper(
        uses = TagMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface GiftCertificateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "createDate", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "lastUpdateDate", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "duration", expression = "java(java.time.Duration.ofDays(giftCertificateDtoRequest.getDuration()))")
    GiftCertificate toGiftCertificate(GiftCertificateDtoRequest giftCertificateDtoRequest);

    @Mapping(target = "duration", expression = "java(giftCertificate.getDuration().toDays())")
    GiftCertificateDtoResponse toGiftCertificateDtoResponse(GiftCertificate giftCertificate);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(
            target = "duration",
            expression = "java(giftCertificateDtoRequest.getDuration() != null ? " +
                    "java.time.Duration.ofDays(giftCertificateDtoRequest.getDuration()) : " +
                    "giftCertificate.getDuration())")
    void updateGiftCertificate(
            GiftCertificateDtoRequest giftCertificateDtoRequest,
            @MappingTarget GiftCertificate giftCertificate
    );
}
