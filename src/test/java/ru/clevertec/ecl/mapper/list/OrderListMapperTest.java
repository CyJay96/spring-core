package ru.clevertec.ecl.mapper.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.order.OrderDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.order.OrderTestBuilder;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.entity.Order;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderListMapperTest {

    private OrderListMapper orderListMapper;

    @Mock
    private OrderMapper orderMapper;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @BeforeEach
    void setUp() {
        orderListMapper = new OrderListMapperImpl(orderMapper);
    }

    @Test
    @DisplayName("Map Order List Entity to DTO")
    void checkToDtoShouldReturnOrderDtoResponseList() {
        Order order = OrderTestBuilder.aOrder().build();
        OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

        when(orderMapper.toDto(any())).thenReturn(orderDtoResponse);

        List<OrderDtoResponse> orderDtoResponseList = orderListMapper.toDto(List.of(order));

        verify(orderMapper).toDto(orderCaptor.capture());

        assertAll(
                () -> assertThat(Objects.requireNonNull(orderDtoResponseList).size()).isEqualTo(1),
                () -> assertThat(Objects.requireNonNull(orderDtoResponseList).get(0)).isEqualTo(orderDtoResponse),
                () -> assertThat(Objects.requireNonNull(orderCaptor).getValue()).isEqualTo(order)
        );
    }
}
