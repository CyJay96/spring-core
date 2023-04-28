package ru.clevertec.ecl.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.repository.OrderRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class OrderRepositoryTest extends BaseIntegrationTest {

    private final OrderRepository orderRepository;

    @Nested
    public class FindFirstByOrderByIdAscTest {
        @Test
        @DisplayName("Find first Order order by ID ASC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyOrderOptional() {
            List<Order> orders = orderRepository.findAll();

            Order expectedOrder = orders.stream()
                    .sorted(Comparator.comparing(Order::getId))
                    .limit(1)
                    .toList()
                    .get(0);

            Order actualOrder = orderRepository.findFirstByOrderByIdAsc().get();

            assertThat(actualOrder.getId()).isEqualTo(expectedOrder.getId());
        }

        @Test
        @DisplayName("Find first Order order by ID ASC; not found")
        void checkFindFirstByOrderByIdAscShouldReturnEmptyOrderOptional() {
            orderRepository.deleteAll();
            Optional<Order> actualOrderOptional = orderRepository.findFirstByOrderByIdAsc();
            assertThat(actualOrderOptional.isEmpty()).isTrue();
        }
    }

    @Nested
    public class FindFirstByOrderByIdDescTest {
        @Test
        @DisplayName("Find first Order order by ID DESC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyOrderOptional() {
            List<Order> orders = orderRepository.findAll();

            Order expectedOrder = orders.stream()
                    .sorted(Comparator.comparing(Order::getId).reversed())
                    .limit(1)
                    .toList()
                    .get(0);

            Order actualOrder = orderRepository.findFirstByOrderByIdDesc().get();

            assertThat(actualOrder.getId()).isEqualTo(expectedOrder.getId());
        }

        @Test
        @DisplayName("Find first Order order by ID DESC; not found")
        void checkFindFirstByOrderByIdDescShouldReturnEmptyOrderOptional() {
            orderRepository.deleteAll();
            Optional<Order> actualOrderOptional = orderRepository.findFirstByOrderByIdDesc();
            assertThat(actualOrderOptional.isEmpty()).isTrue();
        }
    }
}
