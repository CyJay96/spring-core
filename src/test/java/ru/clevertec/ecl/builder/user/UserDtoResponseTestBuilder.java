package ru.clevertec.ecl.builder.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.dto.response.UserDtoResponse;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_EMAIL;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserDtoResponse")
public class UserDtoResponseTestBuilder implements TestBuilder<UserDtoResponse> {

    private Long id = TEST_ID;

    private String username = TEST_STRING;

    private String firstName = TEST_STRING;

    private String lastName = TEST_STRING;

    private String email = TEST_EMAIL;

    private String status = TEST_STRING;

    private OffsetDateTime createDate = TEST_DATE;

    private OffsetDateTime lastUpdateDate = TEST_DATE;

    private List<Long> ordersIds = Collections.emptyList();

    @Override
    public UserDtoResponse build() {
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setId(id);
        userDtoResponse.setUsername(username);
        userDtoResponse.setFirstName(firstName);
        userDtoResponse.setLastName(lastName);
        userDtoResponse.setEmail(email);
        userDtoResponse.setStatus(status);
        userDtoResponse.setCreateDate(createDate);
        userDtoResponse.setLastUpdateDate(lastUpdateDate);
        userDtoResponse.setOrdersIds(ordersIds);
        return userDtoResponse;
    }
}
