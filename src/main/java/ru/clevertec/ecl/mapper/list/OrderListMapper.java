package ru.clevertec.ecl.mapper.list;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.entity.Order;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderListMapper {

    List<OrderDtoResponse> toDto(List<Order> orderList);
}
