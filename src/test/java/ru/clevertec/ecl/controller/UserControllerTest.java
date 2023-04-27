package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.user.UserDtoResponseTestBuilder;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.EntityNotFoundException;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private PaginationProperties paginationProperties;

    private final UserDtoResponse expectedUserDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, paginationProperties);
    }

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserDtoResponseList() {
        PageResponse<UserDtoResponse> pageResponse = PageResponse.<UserDtoResponse>builder()
                .content(List.of(expectedUserDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(userService).findAll(PAGE, PAGE_SIZE);

        var actualUsers = userController.findAll(PAGE, PAGE_SIZE);

        verify(userService).findAll(anyInt(), anyInt());

        assertAll(
                () -> assertThat(actualUsers.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualUsers.getBody()).getData().getContent().stream()
                        .anyMatch(actualUserDtoResponse -> actualUserDtoResponse.equals(expectedUserDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) {
            doReturn(expectedUserDtoResponse).when(userService).findById(id);

            var actualUser = userController.findById(id);

            verify(userService).findById(anyLong());

            assertAll(
                    () -> assertThat(actualUser.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualUser.getBody()).getData())
                            .isEqualTo(expectedUserDtoResponse)
            );
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindByIdShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userService).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> userController.findById(TEST_ID));

            verify(userService).findById(anyLong());
        }
    }

    @Nested
    public class FindByHighestOrderCostTest {
        @Test
        @DisplayName("Find User by highest order cost")
        void checkFindByHighestOrderCostShouldReturnUserDtoResponse() {
            doReturn(expectedUserDtoResponse).when(userService).findByHighestOrderCost();

            var actualUser = userController.findByHighestOrderCost();

            verify(userService).findByHighestOrderCost();

            assertAll(
                    () -> assertThat(actualUser.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualUser.getBody()).getData())
                            .isEqualTo(expectedUserDtoResponse)
            );
        }

        @Test
        @DisplayName("Find User by highest order cost; not found")
        void checkFindByHighestOrderCostShouldThrowUserNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userService).findByHighestOrderCost();

            assertThrows(EntityNotFoundException.class, () -> userController.findByHighestOrderCost());

            verify(userService).findByHighestOrderCost();
        }
    }
}
