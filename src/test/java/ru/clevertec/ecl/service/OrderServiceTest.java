package ru.clevertec.ecl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.builder.order.OrderDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.order.OrderTestBuilder;
import ru.clevertec.ecl.builder.user.UserTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.model.entity.User;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.impl.OrderServiceImpl;
import ru.clevertec.ecl.util.TestConstants;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private OrderMapper orderMapper;

    private final User user = UserTestBuilder.aUser().build();
    private final GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
    private final Order order = OrderTestBuilder.aOrder().build();
    private final OrderDtoResponse expectedOrderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, userRepository, giftCertificateRepository, orderMapper);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Order")
        void checkSaveByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
            doReturn(Optional.of(user)).when(userRepository).findById(TEST_ID);
            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(TEST_ID);
            doReturn(order).when(orderRepository).save(any());
            doReturn(expectedOrderDtoResponse).when(orderMapper).toOrderDtoResponse(order);

            OrderDtoResponse actualOrder = orderService.saveByUserIdAndGiftCertificateId(TEST_ID, TEST_ID);

            verify(userRepository).findById(anyLong());
            verify(giftCertificateRepository).findById(anyLong());
            verify(orderRepository).save(any());
            verify(orderMapper).toOrderDtoResponse(any());

            assertThat(Objects.requireNonNull(actualOrder)).isEqualTo(expectedOrderDtoResponse);
        }

        @Test
        @DisplayName("Save Order; User not found")
        void checkSaveByUserIdAndGiftCertificateIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> orderService.saveByUserIdAndGiftCertificateId(TEST_ID, TEST_ID)
            );

            verify(userRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Save Order; Gift Certificate not found")
        void checkSaveByUserIdAndGiftCertificateIdShouldThrowOrderNotFoundException() {
            doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
            doThrow(EntityNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> orderService.saveByUserIdAndGiftCertificateId(TEST_ID, TEST_ID)
            );

            verify(userRepository).findById(anyLong());
            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Test
    @DisplayName("Find all Orders")
    void checkFindAllShouldReturnOrderDtoResponseList() {
        doReturn(new PageImpl<>(List.of(order))).when(orderRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        doReturn(expectedOrderDtoResponse).when(orderMapper).toOrderDtoResponse(order);

        PageResponse<OrderDtoResponse> actualOrders = orderService.findAll(PAGE, PAGE_SIZE);

        verify(orderRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        verify(orderMapper).toOrderDtoResponse(order);

        assertThat(Objects.requireNonNull(actualOrders).getContent().stream()
                .anyMatch(actualOrderDtoResponse -> actualOrderDtoResponse.equals(expectedOrderDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindAllOrdersByUserIdTest {
        @Test
        @DisplayName("Find all Orders by User ID")
        void checkFindAllByUserIdShouldReturnOrderDtoResponseList() {
            doReturn(true).when(userRepository).existsById(TEST_ID);
            doReturn(new PageImpl<>(List.of(order)))
                    .when(orderRepository).findAllByUserId(TEST_ID, PageRequest.of(PAGE, PAGE_SIZE));
            doReturn(expectedOrderDtoResponse).when(orderMapper).toOrderDtoResponse(order);

            PageResponse<OrderDtoResponse> actualOrders = orderService.findAllByUserId(TEST_ID, PAGE, PAGE_SIZE);

            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findAllByUserId(TEST_ID, PageRequest.of(PAGE, PAGE_SIZE));
            verify(orderMapper).toOrderDtoResponse(any());

            assertThat(Objects.requireNonNull(actualOrders).getContent().stream()
                    .anyMatch(orderDto -> orderDto.equals(expectedOrderDtoResponse))
            ).isTrue();
        }

        @Test
        @DisplayName("Find all Orders by User ID; User not found")
        void checkFindAllByUserIdShouldThrowUserNotFoundException() {
            doReturn(false).when(userRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> orderService.findAllByUserId(TEST_ID, PAGE, PAGE_SIZE));

            verify(userRepository).existsById(anyLong());
        }
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Order by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnOrderDtoResponse(Long id) {
            doReturn(Optional.of(order)).when(orderRepository).findById(id);
            doReturn(expectedOrderDtoResponse).when(orderMapper).toOrderDtoResponse(order);

            OrderDtoResponse actualOrder = orderService.findById(id);

            verify(orderRepository).findById(anyLong());
            verify(orderMapper).toOrderDtoResponse(any());

            assertThat(Objects.requireNonNull(actualOrder)).isEqualTo(expectedOrderDtoResponse);
        }

        @Test
        @DisplayName("Find Order by ID; Order not found")
        void checkGetAllOrdersByUserIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(orderRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> orderService.findById(TEST_ID));

            verify(orderRepository).findById(anyLong());
        }
    }

    @Nested
    public class FindByIdAndUserIdTest {
        @Test
        @DisplayName("Find Order by ID & User ID")
        void checkFindByIdAndUserIdShouldReturnOrderDtoResponse() {
            doReturn(true).when(orderRepository).existsById(TEST_ID);
            doReturn(true).when(userRepository).existsById(TestConstants.TEST_ID);
            doReturn(Optional.of(order)).when(orderRepository).findByIdAndUserId(TEST_ID, TestConstants.TEST_ID);
            doReturn(expectedOrderDtoResponse).when(orderMapper).toOrderDtoResponse(order);

            OrderDtoResponse actualOrder = orderService.findByIdAndUserId(TEST_ID, TEST_ID);

            verify(orderRepository).existsById(anyLong());
            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findByIdAndUserId(anyLong(), anyLong());
            verify(orderMapper).toOrderDtoResponse(any());

            assertThat(Objects.requireNonNull(actualOrder)).isEqualTo(expectedOrderDtoResponse);
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order not found")
        void checkFindByIdAndUserIdShouldThrowOrderNotFoundException() {
            doReturn(false).when(orderRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> orderService.findByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderRepository).existsById(anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; User not found")
        void checkFindByIdAndUserIdShouldThrowUserNotFoundException() {
            doReturn(true).when(orderRepository).existsById(anyLong());
            doReturn(false).when(userRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> orderService.findByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderRepository).existsById(anyLong());
            verify(userRepository).existsById(anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order by User not found")
        void checkFindByIdAndUserIdShouldThrowOrderByUserNotFoundException() {
            doReturn(true).when(orderRepository).existsById(anyLong());
            doReturn(true).when(userRepository).existsById(anyLong());
            doThrow(OrderByUserNotFoundException.class).when(orderRepository).findByIdAndUserId(anyLong(), anyLong());

            assertThrows(OrderByUserNotFoundException.class, () -> orderService.findByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderRepository).existsById(anyLong());
            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findByIdAndUserId(anyLong(), anyLong());
        }
    }
}
