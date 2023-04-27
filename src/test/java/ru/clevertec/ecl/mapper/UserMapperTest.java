package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.builder.user.UserTestBuilder;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;
import ru.clevertec.ecl.model.entity.User;
import ru.clevertec.ecl.model.enums.Status;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_EMAIL;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

class UserMapperTest {

    private UserMapper userMapper;

    private final User user = UserTestBuilder.aUser().build();

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    @DisplayName("Map User Entity to DTO")
    void checkToUserDtoResponseShouldReturnUserDtoResponse() {
        UserDtoResponse userDtoResponse = userMapper.toUserDtoResponse(user);

        assertAll(
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getId()).isEqualTo(TEST_ID),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getUsername()).isEqualTo(TEST_STRING),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getFirstName()).isEqualTo(TEST_STRING),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getLastName()).isEqualTo(TEST_STRING),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getEmail()).isEqualTo(TEST_EMAIL),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getStatus()).isEqualTo(Status.ACTIVE.name().toLowerCase()),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getCreateDate()).isEqualTo(TEST_DATE.toString()),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getLastUpdateDate()).isEqualTo(TEST_DATE.toString()),
                () -> assertThat(Objects.requireNonNull(userDtoResponse).getOrdersIds()).isNullOrEmpty()
        );
    }
}
