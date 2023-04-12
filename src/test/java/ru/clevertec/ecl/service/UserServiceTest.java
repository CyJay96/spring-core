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
import ru.clevertec.ecl.builder.user.UserDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.user.UserTestBuilder;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.model.entity.User;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.impl.UserServiceImpl;

import java.util.List;
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
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    @DisplayName("Get all Users")
    void checkGetAllUsersShouldReturnUserDtoResponseList() {
        User user = UserTestBuilder.aUser().build();
        UserDtoResponse userDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

        when(userRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE))).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(userDtoResponse);

        PageResponse<UserDtoResponse> response = userService.getAllUsers(PAGE, PAGE_SIZE);

        verify(userRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        verify(userMapper).toDto(any());

        assertThat(response.getContent().get(0)).isEqualTo(userDtoResponse);
    }

    @Nested
    public class GetUserByIdTest {
        @DisplayName("Get User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetUserByIdShouldReturnUserDtoResponse(Long id) {
            User user = UserTestBuilder.aUser().build();
            UserDtoResponse userDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(userDtoResponse);

            UserDtoResponse response = userService.getUserById(id);

            verify(userRepository).findById(anyLong());
            verify(userMapper).toDto(any());

            assertThat(response).isEqualTo(userDtoResponse);
        }

        @Test
        @DisplayName("Get User by ID; not found")
        void checkGetUserByIdShouldThrowTagNotFoundException() {
            doThrow(UserNotFoundException.class).when(userRepository).findById(anyLong());

            assertThrows(UserNotFoundException.class, () -> userService.getUserById(TEST_ID));

            verify(userRepository).findById(anyLong());
        }
    }

    @Nested
    public class GetUserByHighestOrderCostTest {
        @Test
        @DisplayName("Get User by highest order cost")
        void checkGetUserByHighestOrderCostShouldReturnUserDtoResponse() {
            User user = UserTestBuilder.aUser().build();
            UserDtoResponse userDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

            when(userRepository.findUserByHighestOrderCost()).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(userDtoResponse);

            UserDtoResponse response = userService.getUserByHighestOrderCost();

            verify(userRepository).findUserByHighestOrderCost();
            verify(userMapper).toDto(any());

            assertThat(response).isEqualTo(userDtoResponse);
        }

        @Test
        @DisplayName("Get User by highest order cost; not found")
        void checkGetUserByHighestOrderCostShouldThrowTagNotFoundException() {
            doThrow(UserNotFoundException.class).when(userRepository).findUserByHighestOrderCost();

            assertThrows(UserNotFoundException.class, () -> userService.getUserByHighestOrderCost());

            verify(userRepository).findUserByHighestOrderCost();
        }
    }
}
