package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.order.OrderDtoResponseTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.util.TestConstants;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private final OrderDtoResponse expectedOrderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();
    private final Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderService);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Order")
        void checkSaveByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
            doReturn(expectedOrderDtoResponse).when(orderService).saveByUserIdAndGiftCertificateId(TEST_ID, TEST_ID);

            var actualOrder = orderController
                    .saveByUserIdAndGiftCertificateId(TEST_ID, TEST_ID);

            verify(orderService).saveByUserIdAndGiftCertificateId(anyLong(), anyLong());

            assertAll(
                    () -> assertThat(actualOrder.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(actualOrder.getBody()).getData())
                            .isEqualTo(expectedOrderDtoResponse)
            );
        }

        @Test
        @DisplayName("Save Order; User not found")
        void checkSaveByUserIdAndGiftCertificateIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class)
                    .when(orderService).saveByUserIdAndGiftCertificateId(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> orderController.saveByUserIdAndGiftCertificateId(TEST_ID, TEST_ID)
            );

            verify(orderService).saveByUserIdAndGiftCertificateId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Save Order; Gift Certificate not found")
        void checkSaveByUserIdAndGiftCertificateIdShouldThrowOrderNotFoundException() {
            doThrow(EntityNotFoundException.class)
                    .when(orderService).saveByUserIdAndGiftCertificateId(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> orderController.saveByUserIdAndGiftCertificateId(TEST_ID, TEST_ID)
            );

            verify(orderService).saveByUserIdAndGiftCertificateId(anyLong(), anyLong());
        }
    }

    @Test
    @DisplayName("Find all Orders")
    void checkFindAllShouldReturnOrderDtoResponseList() {
        PageResponse<OrderDtoResponse> pageResponse = PageResponse.<OrderDtoResponse>builder()
                .content(List.of(expectedOrderDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(orderService).findAll(pageable);

        var actualOrders = orderController.findAll(pageable);

        verify(orderService).findAll(any());

        assertAll(
                () -> assertThat(actualOrders.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualOrders.getBody()).getData().getContent().stream()
                        .anyMatch(orderDto -> orderDto.equals(expectedOrderDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindAllByUserIdTest {
        @Test
        @DisplayName("Find all Orders by User ID")
        void checkFindAllByUserIdShouldReturnOrderDtoResponseList() {
            PageResponse<OrderDtoResponse> pageResponse = PageResponse.<OrderDtoResponse>builder()
                    .content(List.of(expectedOrderDtoResponse))
                    .number(PAGE)
                    .size(PAGE_SIZE)
                    .numberOfElements(1)
                    .build();

            doReturn(pageResponse). when(orderService).findAllByUserId(TEST_ID, pageable);

            var actualOrders = orderController.findAllByUserId(TEST_ID, pageable);

            verify(orderService).findAllByUserId(anyLong(), any());

            assertAll(
                    () -> assertThat(actualOrders.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualOrders.getBody()).getData().getContent().stream()
                            .anyMatch(orderDto -> orderDto.equals(expectedOrderDtoResponse))
                    ).isTrue()
            );
        }

        @Test
        @DisplayName("Find all Orders by User ID; User not found")
        void checkFindAllByUserIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(orderService).findAllByUserId(anyLong(), any());

            assertThrows(EntityNotFoundException.class,
                    () -> orderController.findAllByUserId(TEST_ID, pageable)
            );

            verify(orderService).findAllByUserId(anyLong(), any());
        }
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Order by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnOrderDtoResponse(Long id) {
            doReturn(expectedOrderDtoResponse).when(orderService).findById(id);

            var actualOrder = orderController.findById(id);

            verify(orderService).findById(anyLong());

            assertAll(
                    () -> assertThat(actualOrder.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualOrder.getBody()).getData())
                            .isEqualTo(expectedOrderDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Order by ID; not found")
        void checkFindByIdShouldThrowOrderNotFoundException() {
            doThrow(EntityNotFoundException.class).when(orderService).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> orderController.findById(TEST_ID));

            verify(orderService).findById(anyLong());
        }
    }

    @Nested
    public class FindByIdAndUserIdTest {
        @Test
        @DisplayName("Find Order by ID & User ID")
        void checkFindByIdAndUserIdShouldReturnOrderDtoResponse() {
            doReturn(expectedOrderDtoResponse).when(orderService).findByIdAndUserId(TEST_ID, TestConstants.TEST_ID);

            var actualOrder = orderController.findByIdAndUserId(TEST_ID, TestConstants.TEST_ID);

            verify(orderService).findByIdAndUserId(anyLong(), anyLong());

            assertAll(
                    () -> assertThat(actualOrder.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualOrder.getBody()).getData())
                            .isEqualTo(expectedOrderDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order not found")
        void checkFindByIdAndUserIdShouldThrowOrderNotFoundException() {
            doThrow(EntityNotFoundException.class).when(orderService).findByIdAndUserId(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class, () -> orderController.findByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).findByIdAndUserId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; User not found")
        void checkFindByIdAndUserIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(orderService).findByIdAndUserId(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class, () -> orderController.findByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).findByIdAndUserId(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order by User not found")
        void checkFindByIdAndUserIdShouldThrowOrderByUserNotFoundException() {
            doThrow(OrderByUserNotFoundException.class).when(orderService).findByIdAndUserId(anyLong(), anyLong());

            assertThrows(OrderByUserNotFoundException.class, () -> orderController.findByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderService).findByIdAndUserId(anyLong(), anyLong());
        }
    }
}
