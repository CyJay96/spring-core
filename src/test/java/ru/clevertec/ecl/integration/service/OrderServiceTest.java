package ru.clevertec.ecl.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.OrderService;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceTest extends BaseIntegrationTest {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Nested
    public class CreateOrderTest {
        @Test
        @DisplayName("Create Order")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long expectedOrderId = orderRepository.findFirstByOrderByIdDesc().get().getId() + 1;

            OrderDtoResponse actualOrder = orderService.createOrderByUserIdAndGiftCertificateId(existsUserId, existsGiftCertificateId);

            assertThat(actualOrder.getId()).isEqualTo(expectedOrderId);
        }

        @Test
        @DisplayName("Create Order; User not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowUserNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            assertThrows(UserNotFoundException.class, () -> orderService.createOrderByUserIdAndGiftCertificateId(doesntExistUserId, existsGiftCertificateId));
        }

        @Test
        @DisplayName("Create Order; Gift Certificate not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowOrderNotFoundException() {
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(GiftCertificateNotFoundException.class, () -> orderService.createOrderByUserIdAndGiftCertificateId(existsUserId, doesntExistGiftCertificateId));
        }
    }

    @Test
    @DisplayName("Get all Orders")
    void checkGetAllOrdersShouldReturnOrderDtoResponseList() {
        int expectedOrdersSize = (int) orderRepository.count();
        PageResponse<OrderDtoResponse> actualOrders = orderService.getAllOrders(PAGE, PAGE_SIZE);
        assertThat(actualOrders.getContent()).hasSize(expectedOrdersSize);
    }

    @Nested
    public class GetAllOrdersByUserIdTest {
        @Test
        @DisplayName("Get all Orders by User ID")
        void checkGetAllOrdersByUserIdShouldReturnOrderDtoResponseList() {
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            int expectedOrdersSize = orderRepository.findAllByUserId(existsUserId, PageRequest.of(PAGE, PAGE_SIZE)).getNumberOfElements();
            PageResponse<OrderDtoResponse> actualOrders = orderService.getAllOrdersByUserId(existsUserId, PAGE, PAGE_SIZE);
            assertAll(
                    () -> assertThat(actualOrders.getContent()).hasSize(expectedOrdersSize),
                    () -> assertThat(actualOrders.getContent().stream()
                            .map(OrderDtoResponse::getUserId)
                            .allMatch(userId -> userId.equals(existsUserId))
                    ).isTrue()
            );
        }

        @Test
        @DisplayName("Get all Orders by User ID; User not found")
        void checkGetAllOrdersByUserIdShouldThrowUserNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(UserNotFoundException.class, () -> orderService.getAllOrdersByUserId(doesntExistUserId, PAGE, PAGE_SIZE));
        }
    }

    @Nested
    public class GetOrderByIdTest {
        @DisplayName("Get Order by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetOrderByIdShouldReturnOrderDtoResponse(Long id) {
            OrderDtoResponse actualOrder = orderService.getOrderById(id);
            assertThat(actualOrder.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Get Order by ID; Order not found")
        void checkGetAllOrdersByUserIdShouldThrowUserNotFoundException() {
            Long doesntExistOrderId = new Random()
                    .nextLong(orderRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(doesntExistOrderId));
        }
    }

    @Nested
    public class GetOrderByIdAndUserIdTest {
        @DisplayName("Get Order by ID & User ID")
        @Test
        void checkGetOrderByIdAndUserIdShouldReturnOrderDtoResponse() {
            Order existsOrder = orderRepository.findFirstByOrderByIdAsc().get();
            Long existsOrderId = existsOrder.getId();
            Long existsUserId = existsOrder.getUser().getId();

            OrderDtoResponse actualOrder = orderService.getOrderByIdAndUserId(existsOrderId, existsUserId);

            assertAll(
                    () -> assertThat(actualOrder.getId()).isEqualTo(existsOrderId),
                    () -> assertThat(actualOrder.getUserId()).isEqualTo(existsUserId)
            );
        }

        @Test
        @DisplayName("Get Order by ID & User ID; User not found")
        void checkGetOrderByIdAndUserIdShouldThrowUserNotFoundException() {
            Long existsOrderId = orderRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(UserNotFoundException.class, () -> orderService.getOrderByIdAndUserId(existsOrderId, doesntExistUserId));
        }

        @Test
        @DisplayName("Get Order by ID & User ID; Order not found")
        void checkGetOrderByIdAndUserIdShouldThrowOrderNotFoundException() {
            Long doesntExistOrderId = new Random()
                    .nextLong(orderRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            assertThrows(OrderNotFoundException.class, () -> orderService.getOrderByIdAndUserId(doesntExistOrderId, existsUserId));
        }

        @Test
        @DisplayName("Get Order by ID & User ID; Order by User not found")
        void checkGetOrderByIdAndUserIdShouldThrowOrderByUserNotFoundException() {
            Long existsOrderId = orderRepository.findFirstByOrderByIdAsc().get().getId();
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId() + 1;
            assertThrows(OrderByUserNotFoundException.class, () -> orderService.getOrderByIdAndUserId(existsOrderId, existsUserId));
        }
    }
}
