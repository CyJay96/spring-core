package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.mapper.list.OrderListMapper;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.model.entity.User;

@Mapper(componentModel = "spring", uses = OrderListMapper.class)
public interface UserMapper {

    @Mapping(target = "status", expression = "java(user.getStatus().name().toLowerCase())")
    @Mapping(target = "ordersIds", expression = "java(user.getOrders().stream().map(order -> order.getId()).toList())")
    UserDtoResponse toDto(User user);
}
