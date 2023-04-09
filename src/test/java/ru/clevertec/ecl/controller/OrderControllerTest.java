package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.order.OrderDtoResponseTestBuilder;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.service.OrderService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private PaginationProperties paginationProperties;

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderService, paginationProperties);
    }

    @Test
    @DisplayName("Create Order")
    void checkCreateOrderByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
        OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

        when(orderService.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID)).thenReturn(orderDtoResponse);

        var orderDto = orderController.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID);

        verify(orderService).createOrderByUserIdAndGiftCertificateId(anyLong(), anyLong());

        assertAll(
                () -> assertThat(orderDto.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(Objects.requireNonNull(orderDto.getBody()).getData()).isEqualTo(orderDtoResponse)
        );
    }

    @Test
    @DisplayName("Find all Orders")
    void checkFindAllOrdersShouldReturnOrderDtoResponseList() {
        OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

        PageResponse<OrderDtoResponse> pageResponse = PageResponse.<OrderDtoResponse>builder()
                .content(List.of(orderDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        when(orderService.getAllOrders(PAGE, PAGE_SIZE)).thenReturn(pageResponse);

        var orderDtoList = orderController.findAllOrders(PAGE, PAGE_SIZE);

        verify(orderService).getAllOrders(anyInt(), anyInt());

        assertAll(
                () -> assertThat(orderDtoList.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(orderDtoList.getBody()).getData().getContent().get(0)).isEqualTo(orderDtoResponse)
        );
    }

    @Nested
    public class FindAllOrdersByUserIdTest {
        @Test
        @DisplayName("Find all Orders by User ID")
        void checkFindAllOrdersByUserIdShouldReturnOrderDtoResponseList() {
            OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

            PageResponse<OrderDtoResponse> pageResponse = PageResponse.<OrderDtoResponse>builder()
                    .content(List.of(orderDtoResponse))
                    .number(PAGE)
                    .size(PAGE_SIZE)
                    .numberOfElements(1)
                    .build();

            when(orderService.getAllOrdersByUserId(TEST_ID, PAGE, PAGE_SIZE)).thenReturn(pageResponse);

            var orderDtoList = orderController.findAllOrdersByUserId(TEST_ID, PAGE, PAGE_SIZE);

            verify(orderService).getAllOrdersByUserId(anyLong(), anyInt(), anyInt());

            assertAll(
                    () -> assertThat(orderDtoList.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(orderDtoList.getBody()).getData().getContent().get(0)).isEqualTo(orderDtoResponse)
            );
        }

        @Test
        @DisplayName("Find all Orders by User ID; User not found")
        void checkFindAllOrdersByUserIdShouldThrowUserNotFoundException() {
            doThrow(UserNotFoundException.class).when(orderService).getAllOrdersByUserId(anyLong(), anyInt(), anyInt());

            assertThrows(UserNotFoundException.class, () -> orderController.findAllOrdersByUserId(TEST_ID, PAGE, PAGE_SIZE));

            verify(orderService).getAllOrdersByUserId(anyLong(), anyInt(), anyInt());
        }
    }

    @Nested
    public class FindOrderByIdTest {
        @DisplayName("Find Order by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindOrderByIdShouldReturnOrderDtoResponse(Long id) {
            OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse()
                    .withId(id)
                    .build();

            when(orderService.getOrderById(id)).thenReturn(orderDtoResponse);

            var orderDto = orderController.findOrderById(id);

            verify(orderService).getOrderById(anyLong());

            assertAll(
                    () -> assertThat(orderDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(orderDto.getBody()).getData()).isEqualTo(orderDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Order by ID; Order not found")
        void checkFindAllOrdersByUserIdShouldThrowUserNotFoundException() {
            doThrow(OrderNotFoundException.class).when(orderService).getOrderById(anyLong());

            assertThrows(OrderNotFoundException.class, () -> orderController.findOrderById(TEST_ID));

            verify(orderService).getOrderById(anyLong());
        }
    }

    @Nested
    public class FindOrderByIdAndUserIdTest {
        @DisplayName("Find Order by ID & User ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindOrderByIdAndUserIdShouldReturnOrderDtoResponse(Long id) {
            OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse()
                    .withId(id)
                    .build();

            when(orderService.getOrderByIdAndUserId(id, TEST_ID)).thenReturn(orderDtoResponse);

            var orderDto = orderController.findOrderByIdAndUserId(id, TEST_ID);

            verify(orderService).getOrderByIdAndUserId(anyLong(), anyLong());

            assertAll(
                    () -> assertThat(orderDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(orderDto.getBody()).getData()).isEqualTo(orderDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Order by ID & User ID; User not found")
        void checkFindOrderByIdAndUserIdShouldThrowUserNotFoundException() {
            doThrow(UserNotFoundException.class).when(orderService).getOrderByIdAndUserId(anyLong(), anyLong());

            assertThrows(UserNotFoundException.class, () -> orderController.findOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).getOrderByIdAndUserId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order not found")
        void checkFindOrderByIdAndUserIdShouldThrowOrderNotFoundException() {
            doThrow(OrderNotFoundException.class).when(orderService).getOrderByIdAndUserId(anyLong(), anyLong());

            assertThrows(OrderNotFoundException.class, () -> orderController.findOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).getOrderByIdAndUserId(anyLong(), anyLong());
        }
    }
}