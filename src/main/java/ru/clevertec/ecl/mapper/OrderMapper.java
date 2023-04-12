package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", expression = "java(order.getUser().getId())")
    @Mapping(target = "giftCertificateId", expression = "java(order.getGiftCertificate().getId())")
    OrderDtoResponse toDto(Order order);
}
