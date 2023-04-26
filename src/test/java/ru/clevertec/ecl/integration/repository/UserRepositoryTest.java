package ru.clevertec.ecl.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.model.entity.User;
import ru.clevertec.ecl.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryTest extends BaseIntegrationTest {

    private final UserRepository userRepository;

    @Nested
    public class FindUserByHighestOrderCostTest {
        @Test
        @DisplayName("Find User by highest order cost")
        void checkFindUserByHighestOrderCostShouldReturnNotEmptyUserOptional() {
            List<User> users = userRepository.findAll();
            double expectedMaxPrice = users.stream()
                    .map(User::getOrders)
                    .map(orders -> orders.stream()
                            .map(Order::getFinalPrice)
                            .reduce(BigDecimal::add)
                    )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .mapToDouble(BigDecimal::doubleValue)
                    .max()
                    .getAsDouble();

            Optional<User> userOptional = userRepository.findUserByHighestOrderCost();
            double actualMaxPrice = userOptional.get().getOrders().stream()
                    .map(Order::getFinalPrice)
                    .reduce(BigDecimal::add)
                    .get()
                    .doubleValue();

            assertThat(actualMaxPrice).isEqualTo(expectedMaxPrice);
        }

        @Test
        @DisplayName("Find User by highest order cost; not found")
        void checkFindUserByHighestOrderCostShouldReturnEmptyUserOptional() {
            userRepository.deleteAll();
            Optional<User> actualUserOptional = userRepository.findUserByHighestOrderCost();
            assertThat(actualUserOptional.isEmpty()).isTrue();
        }
    }

    @Nested
    public class FindFirstByOrderByIdAscTest {
        @Test
        @DisplayName("Find first User order by ID ASC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyUserOptional() {
            List<User> users = userRepository.findAll();

            User expectedUser = users.stream()
                    .sorted(Comparator.comparing(User::getId))
                    .limit(1)
                    .toList()
                    .get(0);

            User actualUser = userRepository.findFirstByOrderByIdAsc().get();

            assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        }

        @Test
        @DisplayName("Find first User order by ID ASC; not found")
        void checkFindFirstByOrderByIdAscShouldReturnEmptyUserOptional() {
            userRepository.deleteAll();
            Optional<User> actualUserOptional = userRepository.findFirstByOrderByIdAsc();
            assertThat(actualUserOptional.isEmpty()).isTrue();
        }
    }

    @Nested
    public class FindFirstByOrderByIdDescTest {
        @Test
        @DisplayName("Find first User order by ID DESC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyUserOptional() {
            List<User> users = userRepository.findAll();

            User expectedUser = users.stream()
                    .sorted(Comparator.comparing(User::getId).reversed())
                    .limit(1)
                    .toList()
                    .get(0);

            User actualUser = userRepository.findFirstByOrderByIdDesc().get();

            assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        }

        @Test
        @DisplayName("Find first User order by ID DESC; not found")
        void checkFindFirstByOrderByIdDescShouldReturnEmptyUserOptional() {
            userRepository.deleteAll();
            Optional<User> actualUserOptional = userRepository.findFirstByOrderByIdDesc();
            assertThat(actualUserOptional.isEmpty()).isTrue();
        }
    }
}
