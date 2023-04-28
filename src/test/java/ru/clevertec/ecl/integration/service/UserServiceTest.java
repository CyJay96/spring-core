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
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.UserService;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserServiceTest extends BaseIntegrationTest {

    private final UserService userService;
    private final UserRepository userRepository;

    private final Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserDtoResponseList() {
        int expectedUsersSize = (int) userRepository.count();
        PageResponse<UserDtoResponse> actualUsers = userService.findAll(pageable);
        assertThat(actualUsers.getContent()).hasSize(expectedUsersSize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Get User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) {
            UserDtoResponse actualUser = userService.findById(id);
            assertThat(actualUser.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindByIdShouldThrowTagNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> userService.findById(doesntExistUserId));
        }
    }

    @Nested
    public class FindByHighestOrderCostTest {
        @Test
        @DisplayName("Get User by highest order cost")
        void checkFindByHighestOrderCostShouldReturnUserDtoResponse() {
            Long expectedUserId = userRepository.findByHighestOrderCost().get().getId();
            UserDtoResponse actualUser = userService.findByHighestOrderCost();
            assertThat(actualUser.getId()).isEqualTo(expectedUserId);
        }

        @Test
        @DisplayName("Find User by highest order cost; not found")
        void checkFindByHighestOrderCostShouldThrowTagNotFoundException() {
            userRepository.deleteAll();
            assertThrows(EntityNotFoundException.class, userService::findByHighestOrderCost);
        }
    }
}
