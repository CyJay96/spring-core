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
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_BOOLEAN;
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
    private final OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, userRepository, giftCertificateRepository, orderMapper);
    }

    @Nested
    public class CreateOrderTest {
        @Test
        @DisplayName("Create Order")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
            when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(user));
            when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(giftCertificate));
            when(orderRepository.save(any())).thenReturn(order);
            when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

            var orderDto = orderService.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID);

            verify(userRepository).findById(anyLong());
            verify(giftCertificateRepository).findById(anyLong());
            verify(orderRepository).save(any());
            verify(orderMapper).toDto(any());

            assertThat(Objects.requireNonNull(orderDto)).isEqualTo(orderDtoResponse);
        }

        @Test
        @DisplayName("Create Order; User not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowUserNotFoundException() {
            doThrow(UserNotFoundException.class).when(userRepository).findById(anyLong());

            assertThrows(UserNotFoundException.class, () -> orderService.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID));

            verify(userRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Create Order; Gift Certificate not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowOrderNotFoundException() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> orderService.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID));

            verify(userRepository).findById(anyLong());
            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Test
    @DisplayName("Get all Orders")
    void checkGetAllOrdersShouldReturnOrderDtoResponseList() {
        when(orderRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE))).thenReturn(new PageImpl<>(List.of(order)));
        when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

        var orderDtoList = orderService.getAllOrders(PAGE, PAGE_SIZE);

        verify(orderRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        verify(orderMapper).toDto(order);

        assertThat(Objects.requireNonNull(orderDtoList).getContent().stream()
                .anyMatch(orderDto -> orderDto.equals(orderDtoResponse))
        ).isTrue();
    }

    @Nested
    public class GetAllOrdersByUserIdTest {
        @Test
        @DisplayName("Get all Orders by User ID")
        void checkGetAllOrdersByUserIdShouldReturnOrderDtoResponseList() {
            when(userRepository.existsById(TEST_ID)).thenReturn(TEST_BOOLEAN);
            when(orderRepository.findAllByUserId(TEST_ID, PageRequest.of(PAGE, PAGE_SIZE))).thenReturn(new PageImpl<>(List.of(order)));
            when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

            var orderDtoList = orderService.getAllOrdersByUserId(TEST_ID, PAGE, PAGE_SIZE);

            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findAllByUserId(TEST_ID, PageRequest.of(PAGE, PAGE_SIZE));
            verify(orderMapper).toDto(any());

            assertThat(Objects.requireNonNull(orderDtoList).getContent().stream()
                    .anyMatch(orderDto -> orderDto.equals(orderDtoResponse))
            ).isTrue();
        }

        @Test
        @DisplayName("Get all Orders by User ID; User not found")
        void checkGetAllOrdersByUserIdShouldThrowUserNotFoundException() {
            when(userRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(UserNotFoundException.class, () -> orderService.getAllOrdersByUserId(TEST_ID, PAGE, PAGE_SIZE));

            verify(userRepository).existsById(anyLong());
        }
    }

    @Nested
    public class GetOrderByIdTest {
        @DisplayName("Get Order by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetOrderByIdShouldReturnOrderDtoResponse(Long id) {
            when(orderRepository.findById(id)).thenReturn(Optional.of(order));
            when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

            var orderDto = orderService.getOrderById(id);

            verify(orderRepository).findById(anyLong());
            verify(orderMapper).toDto(any());

            assertThat(Objects.requireNonNull(orderDto)).isEqualTo(orderDtoResponse);
        }

        @Test
        @DisplayName("Get Order by ID; Order not found")
        void checkGetAllOrdersByUserIdShouldThrowUserNotFoundException() {
            doThrow(OrderNotFoundException.class).when(orderRepository).findById(anyLong());

            assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(TEST_ID));

            verify(orderRepository).findById(anyLong());
        }
    }

    @Nested
    public class GetOrderByIdAndUserIdTest {
        @Test
        @DisplayName("Get Order by ID & User ID")
        void checkGetOrderByIdAndUserIdShouldReturnOrderDtoResponse() {
            when(orderRepository.existsById(TEST_ID)).thenReturn(TEST_BOOLEAN);
            when(userRepository.existsById(TestConstants.TEST_ID)).thenReturn(TEST_BOOLEAN);
            when(orderRepository.findByIdAndUserId(TEST_ID, TestConstants.TEST_ID)).thenReturn(Optional.of(order));
            when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

            var orderDto = orderService.getOrderByIdAndUserId(TEST_ID, TestConstants.TEST_ID);

            verify(orderRepository).existsById(anyLong());
            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findByIdAndUserId(anyLong(), anyLong());
            verify(orderMapper).toDto(any());

            assertThat(Objects.requireNonNull(orderDto)).isEqualTo(orderDtoResponse);
        }

        @Test
        @DisplayName("Get Order by ID & User ID; Order not found")
        void checkGetOrderByIdAndUserIdShouldThrowOrderNotFoundException() {
            when(orderRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(OrderNotFoundException.class, () -> orderService.getOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderRepository).existsById(anyLong());
        }

        @Test
        @DisplayName("Get Order by ID & User ID; User not found")
        void checkGetOrderByIdAndUserIdShouldThrowUserNotFoundException() {
            when(orderRepository.existsById(anyLong())).thenReturn(TEST_BOOLEAN);
            when(userRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(UserNotFoundException.class, () -> orderService.getOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderRepository).existsById(anyLong());
            verify(userRepository).existsById(anyLong());
        }

        @Test
        @DisplayName("Get Order by ID & User ID; Order by User not found")
        void checkGetOrderByIdAndUserIdShouldThrowOrderByUserNotFoundException() {
            when(orderRepository.existsById(anyLong())).thenReturn(TEST_BOOLEAN);
            when(userRepository.existsById(anyLong())).thenReturn(TEST_BOOLEAN);
            doThrow(OrderByUserNotFoundException.class).when(orderRepository).findByIdAndUserId(anyLong(), anyLong());

            assertThrows(OrderByUserNotFoundException.class, () -> orderService.getOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderRepository).existsById(anyLong());
            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findByIdAndUserId(anyLong(), anyLong());
        }
    }
}
