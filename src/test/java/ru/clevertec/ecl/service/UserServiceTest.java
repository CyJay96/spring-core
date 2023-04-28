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
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.builder.user.UserDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.user.UserTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
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

    private final User user = UserTestBuilder.aUser().build();
    private final UserDtoResponse expectedUserDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();
    private final Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserDtoResponseList() {
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toUserDtoResponse(user)).thenReturn(expectedUserDtoResponse);

        PageResponse<UserDtoResponse> actualUsers = userService.findAll(pageable);

        verify(userRepository).findAll(eq(pageable));
        verify(userMapper).toUserDtoResponse(any());

        assertThat(actualUsers.getContent().stream()
                .anyMatch(actualUserDtoResponse -> actualUserDtoResponse.equals(expectedUserDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) {
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            when(userMapper.toUserDtoResponse(user)).thenReturn(expectedUserDtoResponse);

            UserDtoResponse actualUser = userService.findById(id);

            verify(userRepository).findById(anyLong());
            verify(userMapper).toUserDtoResponse(any());

            assertThat(actualUser).isEqualTo(expectedUserDtoResponse);
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindByIdShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> userService.findById(TEST_ID));

            verify(userRepository).findById(anyLong());
        }
    }

    @Nested
    public class FindByHighestOrderCostTest {
        @Test
        @DisplayName("Find User by highest order cost")
        void checkFindByHighestOrderCostShouldReturnUserDtoResponse() {
            when(userRepository.findByHighestOrderCost()).thenReturn(Optional.of(user));
            when(userMapper.toUserDtoResponse(user)).thenReturn(expectedUserDtoResponse);

            UserDtoResponse actualUser = userService.findByHighestOrderCost();

            verify(userRepository).findByHighestOrderCost();
            verify(userMapper).toUserDtoResponse(any());

            assertThat(actualUser).isEqualTo(expectedUserDtoResponse);
        }

        @Test
        @DisplayName("Find User by highest order cost; not found")
        void checkFindByHighestOrderCostShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(userRepository).findByHighestOrderCost();

            assertThrows(EntityNotFoundException.class, () -> userService.findByHighestOrderCost());

            verify(userRepository).findByHighestOrderCost();
        }
    }
}
