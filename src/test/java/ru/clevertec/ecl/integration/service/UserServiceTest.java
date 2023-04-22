package ru.clevertec.ecl.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.exception.UserNotFoundException;
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
public class UserServiceTest extends BaseIntegrationTest {

    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    @DisplayName("Get all Users")
    void checkGetAllUsersShouldReturnUserDtoResponseList() {
        int expectedUsersSize = (int) userRepository.count();
        PageResponse<UserDtoResponse> actualUsers = userService.getAllUsers(PAGE, PAGE_SIZE);
        assertThat(actualUsers.getContent()).hasSize(expectedUsersSize);
    }

    @Nested
    public class GetUserByIdTest {
        @DisplayName("Get User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetUserByIdShouldReturnUserDtoResponse(Long id) {
            UserDtoResponse actualUser = userService.getUserById(id);
            assertThat(actualUser.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Get User by ID; not found")
        void checkGetUserByIdShouldThrowTagNotFoundException() {
            Long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(UserNotFoundException.class, () -> userService.getUserById(doesntExistUserId));
        }
    }

    @Nested
    public class GetUserByHighestOrderCostTest {
        @Test
        @DisplayName("Get User by highest order cost")
        void checkGetUserByHighestOrderCostShouldReturnUserDtoResponse() {
            Long expectedUserId = userRepository.findUserByHighestOrderCost().get().getId();
            UserDtoResponse actualUser = userService.getUserByHighestOrderCost();
            assertThat(actualUser.getId()).isEqualTo(expectedUserId);
        }

        @Test
        @DisplayName("Get User by highest order cost; not found")
        void checkGetUserByHighestOrderCostShouldThrowTagNotFoundException() {
            userRepository.deleteAll();
            assertThrows(UserNotFoundException.class, userService::getUserByHighestOrderCost);
        }
    }
}
