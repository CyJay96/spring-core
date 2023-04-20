package ru.clevertec.ecl.mapper.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.user.UserDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.user.UserTestBuilder;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.model.entity.User;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserListMapperTest {

    private UserListMapper userListMapper;

    @Mock
    private UserMapper userMapper;

    @Captor
    ArgumentCaptor<User> userCaptor;

    private final User user = UserTestBuilder.aUser().build();
    private final UserDtoResponse userDtoResponse = UserDtoResponseTestBuilder.aUserDtoResponse().build();

    @BeforeEach
    void setUp() {
        userListMapper = new UserListMapperImpl(userMapper);
    }

    @Test
    @DisplayName("Map User List Entity to DTO")
    void checkToDtoShouldReturnUserDtoResponseList() {
        when(userMapper.toDto(any())).thenReturn(userDtoResponse);

        List<UserDtoResponse> userDtoResponseList = userListMapper.toDto(List.of(user));

        verify(userMapper).toDto(userCaptor.capture());

        assertAll(
                () -> assertThat(Objects.requireNonNull(userDtoResponseList).size()).isEqualTo(1),
                () -> assertThat(Objects.requireNonNull(userDtoResponseList).stream()
                        .anyMatch(userDto -> userDto.equals(userDtoResponse))
                ).isTrue(),
                () -> assertThat(Objects.requireNonNull(userCaptor).getValue()).isEqualTo(user)
        );
    }
}
