package ru.clevertec.ecl.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.OrderByUserNotFoundException;
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
class OrderServiceTest extends BaseIntegrationTest {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    private final Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);

    @Nested
    public class CreateOrderTest {
        @Test
        @DisplayName("Save Order")
        void checkSaveByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() {
            Long expectedUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            Long expectedGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();

            OrderDtoResponse actualOrder = orderService
                    .saveByUserIdAndGiftCertificateId(expectedUserId, expectedGiftCertificateId);

            assertAll(
                    () -> assertThat(actualOrder.getUserId()).isEqualTo(expectedUserId),
                    () -> assertThat(actualOrder.getGiftCertificateId()).isEqualTo(expectedGiftCertificateId)
            );
        }

        @Test
        @DisplayName("Save Order; User not found")
        void checkSaveByUserIdAndGiftCertificateIdShouldThrowUserNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            assertThrows(EntityNotFoundException.class,
                    () -> orderService.saveByUserIdAndGiftCertificateId(doesntExistUserId, existsGiftCertificateId)
            );
        }

        @Test
        @DisplayName("Save Order; Gift Certificate not found")
        void checkSaveByUserIdAndGiftCertificateIdShouldThrowOrderNotFoundException() {
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> orderService.saveByUserIdAndGiftCertificateId(existsUserId, doesntExistGiftCertificateId)
            );
        }
    }

    @Test
    @DisplayName("Find all Orders")
    void checkFindAllShouldReturnOrderDtoResponseList() {
        int expectedOrdersSize = (int) orderRepository.count();
        PageResponse<OrderDtoResponse> actualOrders = orderService.findAll(pageable);
        assertThat(actualOrders.getContent()).hasSize(expectedOrdersSize);
    }

    @Nested
    public class FindAllByUserIdTest {
        @Test
        @DisplayName("Find all Orders by User ID")
        void checkFindAllByUserIdShouldReturnOrderDtoResponseList() {
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            int expectedOrdersSize = orderRepository
                    .findAllByUserId(existsUserId, PageRequest.of(PAGE, PAGE_SIZE)).getNumberOfElements();
            PageResponse<OrderDtoResponse> actualOrders = orderService.findAllByUserId(existsUserId, pageable);
            assertAll(
                    () -> assertThat(actualOrders.getContent()).hasSize(expectedOrdersSize),
                    () -> assertThat(actualOrders.getContent().stream()
                            .map(OrderDtoResponse::getUserId)
                            .allMatch(userId -> userId.equals(existsUserId))
                    ).isTrue()
            );
        }

        @Test
        @DisplayName("Find all Orders by User ID; User not found")
        void checkFindAllByUserIdShouldThrowUserNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> orderService.findAllByUserId(doesntExistUserId, pageable)
            );
        }
    }

    @Nested
    public class FindIdTest {
        @DisplayName("Find Order by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnOrderDtoResponse(Long id) {
            OrderDtoResponse actualOrder = orderService.findById(id);
            assertThat(actualOrder.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find Order by ID; Order not found")
        void checkFindAllByUserIdShouldThrowUserNotFoundException() {
            Long doesntExistOrderId = new Random()
                    .nextLong(orderRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> orderService.findById(doesntExistOrderId));
        }
    }

    @Nested
    public class FindByIdAndUserIdTest {
        @DisplayName("Find Order by ID & User ID")
        @Test
        void checkFindByIdAndUserIdShouldReturnOrderDtoResponse() {
            Order existsOrder = orderRepository.findFirstByOrderByIdAsc().get();
            Long existsOrderId = existsOrder.getId();
            Long existsUserId = existsOrder.getUser().getId();

            OrderDtoResponse actualOrder = orderService.findByIdAndUserId(existsOrderId, existsUserId);

            assertAll(
                    () -> assertThat(actualOrder.getId()).isEqualTo(existsOrderId),
                    () -> assertThat(actualOrder.getUserId()).isEqualTo(existsUserId)
            );
        }

        @Test
        @DisplayName("Find Order by ID & User ID; User not found")
        void checkFindByIdAndUserIdShouldThrowUserNotFoundException() {
            Long existsOrderId = orderRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> orderService.findByIdAndUserId(existsOrderId, doesntExistUserId)
            );
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order not found")
        void checkGetOrderByIdAndUserIdShouldThrowOrderNotFoundException() {
            Long doesntExistOrderId = new Random()
                    .nextLong(orderRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            assertThrows(EntityNotFoundException.class,
                    () -> orderService.findByIdAndUserId(doesntExistOrderId, existsUserId)
            );
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order by User not found")
        void checkFindByIdAndUserIdShouldThrowOrderByUserNotFoundException() {
            Long existsOrderId = orderRepository.findFirstByOrderByIdAsc().get().getId();
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId() + 1;
            assertThrows(OrderByUserNotFoundException.class,
                    () -> orderService.findByIdAndUserId(existsOrderId, existsUserId)
            );
        }
    }
}
