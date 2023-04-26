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
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.util.TestConstants;

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

    private final OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderService, paginationProperties);
    }

    @Nested
    public class CreateOrderTest {
        @Test
        @DisplayName("Create Order")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
            when(orderService.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID)).thenReturn(orderDtoResponse);

            var orderDto = orderController.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID);

            verify(orderService).createOrderByUserIdAndGiftCertificateId(anyLong(), anyLong());

            assertAll(
                    () -> assertThat(orderDto.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(orderDto.getBody()).getData()).isEqualTo(orderDtoResponse)
            );
        }

        @Test
        @DisplayName("Create Order; User not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowUserNotFoundException() {
            doThrow(UserNotFoundException.class).when(orderService).createOrderByUserIdAndGiftCertificateId(anyLong(), anyLong());

            assertThrows(UserNotFoundException.class, () -> orderController.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID));

            verify(orderService).createOrderByUserIdAndGiftCertificateId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Create Order; Gift Certificate not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowOrderNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(orderService).createOrderByUserIdAndGiftCertificateId(anyLong(), anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> orderController.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID));

            verify(orderService).createOrderByUserIdAndGiftCertificateId(anyLong(), anyLong());
        }
    }

    @Test
    @DisplayName("Find all Orders")
    void checkFindAllOrdersShouldReturnOrderDtoResponseList() {
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
                () -> assertThat(Objects.requireNonNull(orderDtoList.getBody()).getData().getContent().stream()
                        .anyMatch(orderDto -> orderDto.equals(orderDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindAllOrdersByUserIdTest {
        @Test
        @DisplayName("Find all Orders by User ID")
        void checkFindAllOrdersByUserIdShouldReturnOrderDtoResponseList() {
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
                    () -> assertThat(Objects.requireNonNull(orderDtoList.getBody()).getData().getContent().stream()
                            .anyMatch(orderDto -> orderDto.equals(orderDtoResponse))
                    ).isTrue()
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
        @Test
        @DisplayName("Find Order by ID & User ID")
        void checkFindOrderByIdAndUserIdShouldReturnOrderDtoResponse() {
            when(orderService.getOrderByIdAndUserId(TEST_ID, TestConstants.TEST_ID)).thenReturn(orderDtoResponse);

            var orderDto = orderController.findOrderByIdAndUserId(TEST_ID, TestConstants.TEST_ID);

            verify(orderService).getOrderByIdAndUserId(anyLong(), anyLong());

            assertAll(
                    () -> assertThat(orderDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(orderDto.getBody()).getData()).isEqualTo(orderDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order not found")
        void checkFindOrderByIdAndUserIdShouldThrowOrderNotFoundException() {
            doThrow(OrderNotFoundException.class).when(orderService).getOrderByIdAndUserId(anyLong(), anyLong());

            assertThrows(OrderNotFoundException.class, () -> orderController.findOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).getOrderByIdAndUserId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; User not found")
        void checkFindOrderByIdAndUserIdShouldThrowUserNotFoundException() {
            doThrow(UserNotFoundException.class).when(orderService).getOrderByIdAndUserId(anyLong(), anyLong());

            assertThrows(UserNotFoundException.class, () -> orderController.findOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).getOrderByIdAndUserId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order by User not found")
        void checkFindOrderByIdAndUserIdShouldThrowOrderByUserNotFoundException() {
            doThrow(OrderByUserNotFoundException.class).when(orderService).getOrderByIdAndUserId(anyLong(), anyLong());

            assertThrows(OrderByUserNotFoundException.class, () -> orderController.findOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).getOrderByIdAndUserId(anyLong(), anyLong());
        }
    }
}
