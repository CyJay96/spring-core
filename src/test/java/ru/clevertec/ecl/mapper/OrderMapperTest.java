package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.builder.order.OrderTestBuilder;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.entity.Order;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.ecl.util.TestConstants.TEST_BIG_DECIMAL;
import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapperImpl();
    }

    @Test
    @DisplayName("Map Order Entity to DTO")
    void checkToDtoShouldReturnOrderDtoResponseList() {
        Order order = OrderTestBuilder.aOrder().build();

        OrderDtoResponse orderDtoResponse = orderMapper.toDto(order);

        assertAll(
                () -> assertThat(Objects.requireNonNull(orderDtoResponse).getId()).isEqualTo(TEST_ID),
                () -> assertThat(Objects.requireNonNull(orderDtoResponse).getUserId()).isEqualTo(TEST_ID),
                () -> assertThat(Objects.requireNonNull(orderDtoResponse).getGiftCertificateId()).isEqualTo(TEST_ID),
                () -> assertThat(Objects.requireNonNull(orderDtoResponse).getFinalPrice()).isEqualTo(TEST_BIG_DECIMAL),
                () -> assertThat(Objects.requireNonNull(orderDtoResponse).getCreateDate()).isEqualTo(TEST_DATE.toString()),
                () -> assertThat(Objects.requireNonNull(orderDtoResponse).getLastUpdateDate()).isEqualTo(TEST_DATE.toString())
        );
    }
}
