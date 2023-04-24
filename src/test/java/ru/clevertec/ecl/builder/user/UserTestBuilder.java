package ru.clevertec.ecl.builder.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.model.entity.User;
import ru.clevertec.ecl.model.enums.Status;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_EMAIL;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUser")
public class UserTestBuilder implements TestBuilder<User> {

    private Long id = TEST_ID;

    private String username = TEST_STRING;

    private String firstName = TEST_STRING;

    private String lastName = TEST_STRING;

    private String email = TEST_EMAIL;

    private Status status = Status.ACTIVE;

    private OffsetDateTime createDate = TEST_DATE;

    private OffsetDateTime lastUpdateDate = TEST_DATE;

    private List<Order> orders = Collections.emptyList();

    @Override
    public User build() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setStatus(status);
        user.setCreateDate(createDate);
        user.setLastUpdateDate(lastUpdateDate);
        user.setOrders(orders);
        return user;
    }
}
