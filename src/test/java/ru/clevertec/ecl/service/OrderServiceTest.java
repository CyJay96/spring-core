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

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, userRepository, giftCertificateRepository, orderMapper);
    }

    @Nested
    public class CreateOrderTest {
        @Test
        @DisplayName("Create Order")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
            User user = UserTestBuilder.aUser().build();
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
            Order order = OrderTestBuilder.aOrder().build();
            OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

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
            User user = UserTestBuilder.aUser().build();

            when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(user));
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> orderService.createOrderByUserIdAndGiftCertificateId(TEST_ID, TEST_ID));

            verify(userRepository).findById(anyLong());
            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Test
    @DisplayName("Get all Orders")
    void checkGetAllOrdersShouldReturnOrderDtoResponseList() {
        Order order = OrderTestBuilder.aOrder().build();
        OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

        when(orderRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE))).thenReturn(new PageImpl<>(List.of(order)));
        when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

        var orderDtoList = orderService.getAllOrders(PAGE, PAGE_SIZE);

        verify(orderRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        verify(orderMapper).toDto(order);

        assertThat(Objects.requireNonNull(orderDtoList).getContent().get(0)).isEqualTo(orderDtoResponse);
    }

    @Nested
    public class GetAllOrdersByUserIdTest {
        @Test
        @DisplayName("Get all Orders by User ID")
        void checkGetAllOrdersByUserIdShouldReturnOrderDtoResponseList() {
            Order order = OrderTestBuilder.aOrder().build();
            OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse().build();

            when(userRepository.existsById(TEST_ID)).thenReturn(TEST_BOOLEAN);
            when(orderRepository.findAllByUserId(TEST_ID, PageRequest.of(PAGE, PAGE_SIZE))).thenReturn(new PageImpl<>(List.of(order)));
            when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

            var orderDtoList = orderService.getAllOrdersByUserId(TEST_ID, PAGE, PAGE_SIZE);

            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findAllByUserId(TEST_ID, PageRequest.of(PAGE, PAGE_SIZE));
            verify(orderMapper).toDto(any());

            assertThat(Objects.requireNonNull(orderDtoList).getContent().get(0)).isEqualTo(orderDtoResponse);
        }

        @Test
        @DisplayName("Find all Orders by User ID; User not found")
        void checkFindAllOrdersByUserIdShouldThrowUserNotFoundException() {
            when(userRepository.existsById(TEST_ID)).thenReturn(false);

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
            Order order = OrderTestBuilder.aOrder().build();
            OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse()
                    .withId(id)
                    .build();

            when(orderRepository.findById(id)).thenReturn(Optional.of(order));
            when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

            var orderDto = orderService.getOrderById(id);

            verify(orderRepository).findById(anyLong());
            verify(orderMapper).toDto(any());

            assertThat(Objects.requireNonNull(orderDto)).isEqualTo(orderDtoResponse);
        }

        @Test
        @DisplayName("Find Order by ID; Order not found")
        void checkFindAllOrdersByUserIdShouldThrowUserNotFoundException() {
            doThrow(OrderNotFoundException.class).when(orderRepository).findById(anyLong());

            assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(TEST_ID));

            verify(orderRepository).findById(anyLong());
        }
    }

    @Nested
    public class GetOrderByIdAndUserIdTest {
        @DisplayName("Get Order by ID & User ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetOrderByIdAndUserIdShouldReturnOrderDtoResponse(Long id) {
            Order order = OrderTestBuilder.aOrder().build();
            OrderDtoResponse orderDtoResponse = OrderDtoResponseTestBuilder.aOrderDtoResponse()
                    .withId(id)
                    .build();

            when(userRepository.existsById(TEST_ID)).thenReturn(TEST_BOOLEAN);
            when(orderRepository.findByIdAndUserId(id, TEST_ID)).thenReturn(Optional.of(order));
            when(orderMapper.toDto(order)).thenReturn(orderDtoResponse);

            var orderDto = orderService.getOrderByIdAndUserId(id, TEST_ID);

            verify(userRepository).existsById(anyLong());
            verify(orderRepository).findByIdAndUserId(anyLong(), anyLong());
            verify(orderMapper).toDto(any());

            assertThat(Objects.requireNonNull(orderDto)).isEqualTo(orderDtoResponse);
        }

        @Test
        @DisplayName("Find Order by ID & User ID; User not found")
        void checkFindOrderByIdAndUserIdShouldThrowUserNotFoundException() {
            when(userRepository.existsById(TEST_ID)).thenReturn(false);

            assertThrows(UserNotFoundException.class, () -> orderService.getOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(userRepository).existsById(anyLong());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order not found")
        void checkFindOrderByIdAndUserIdShouldThrowOrderNotFoundException() {
            when(userRepository.existsById(TEST_ID)).thenReturn(TEST_BOOLEAN);
            doThrow(OrderNotFoundException.class).when(orderRepository).findByIdAndUserId(anyLong(), anyLong());

            assertThrows(OrderNotFoundException.class, () -> orderService.getOrderByIdAndUserId(TEST_ID, TEST_ID));

            verify(orderRepository).findByIdAndUserId(anyLong(), anyLong());
        }
    }
}
