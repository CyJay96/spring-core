package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.builder.order.OrderDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.order.OrderTestBuilder;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.entity.Order;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {

    private OrderMapper orderMapper;

    private final Order order = OrderTestBuilder.aOrder().build();
    private final OrderDtoResponse expectedOrderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

    @BeforeEach
    void setUp() {
        orderMapper = Mappers.getMapper(OrderMapper.class);
    }

    @Test
    @DisplayName("Map Order Entity to DTO")
    void checkToOrderDtoResponseShouldReturnOrderDtoResponseList() {
        OrderDtoResponse actualOrderDtoResponse = orderMapper.toOrderDtoResponse(order);
        assertThat(actualOrderDtoResponse).isEqualTo(expectedOrderDtoResponse);
    }
}
