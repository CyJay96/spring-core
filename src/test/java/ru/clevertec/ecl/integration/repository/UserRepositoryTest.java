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
        void checkFindUserByHighestOrderCostShouldReturnUserDtoResponse() {
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
        void checkFindUserByHighestOrderCostShouldThrowTagNotFoundException() {
            userRepository.deleteAll();
            Optional<User> actualUserOptional = userRepository.findUserByHighestOrderCost();
            assertThat(actualUserOptional.isEmpty()).isTrue();
        }
    }
}
