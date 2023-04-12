package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.user.UserDtoResponseTestBuilder;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.service.UserService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private PaginationProperties paginationProperties;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, paginationProperties);
    }

    @Test
    @DisplayName("Find all Users")
    void checkFindAllUsersShouldReturnUserDtoResponseList() {
        UserDtoResponse userDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

        PageResponse<UserDtoResponse> pageResponse = PageResponse.<UserDtoResponse>builder()
                .content(List.of(userDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        when(userService.getAllUsers(PAGE, PAGE_SIZE)).thenReturn(pageResponse);

        var userDtoList = userController.findAllUsers(PAGE, PAGE_SIZE);

        verify(userService).getAllUsers(anyInt(), anyInt());

        assertAll(
                () -> assertThat(userDtoList.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(userDtoList.getBody()).getData().getContent().get(0)).isEqualTo(userDtoResponse)
        );
    }

    @Nested
    public class FindUserByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindUserByIdShouldReturnUserDtoResponse(Long id) {
            UserDtoResponse userDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

            when(userService.getUserById(id)).thenReturn(userDtoResponse);

            var userDto = userController.findUserById(id);

            verify(userService).getUserById(anyLong());

            assertAll(
                    () -> assertThat(userDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(userDto.getBody()).getData()).isEqualTo(userDtoResponse)
            );
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindUserByIdShouldThrowUserNotFoundException() {
            doThrow(UserNotFoundException.class).when(userService).getUserById(anyLong());

            assertThrows(UserNotFoundException.class, () -> userController.findUserById(TEST_ID));

            verify(userService).getUserById(anyLong());
        }
    }

    @Nested
    public class FindUserByHighestOrderCostTest {
        @Test
        @DisplayName("Find User by highest order cost")
        void checkFindUserByHighestOrderCostShouldReturnUserDtoResponse() {
            UserDtoResponse userDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

            when(userService.getUserByHighestOrderCost()).thenReturn(userDtoResponse);

            var userDto = userController.findUserByHighestOrderCost();

            verify(userService).getUserByHighestOrderCost();

            assertAll(
                    () -> assertThat(userDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(userDto.getBody()).getData()).isEqualTo(userDtoResponse)
            );
        }

        @Test
        @DisplayName("Find User by highest order cost; not found")
        void checkFindUserByHighestOrderCostShouldThrowUserNotFoundException() {
            doThrow(UserNotFoundException.class).when(userService).getUserByHighestOrderCost();

            assertThrows(UserNotFoundException.class, () -> userController.findUserByHighestOrderCost());

            verify(userService).getUserByHighestOrderCost();
        }
    }
}
