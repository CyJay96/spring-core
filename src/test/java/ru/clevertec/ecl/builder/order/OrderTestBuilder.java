package ru.clevertec.ecl.builder.order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.builder.user.UserTestBuilder;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.model.entity.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static ru.clevertec.ecl.util.TestConstants.TEST_BIG_DECIMAL;
import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrder")
public class OrderTestBuilder implements TestBuilder<Order> {

    private Long id = TEST_ID;

    private User user = UserTestBuilder.aUser().build();

    private GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

    private BigDecimal finalPrice = TEST_BIG_DECIMAL;

    private OffsetDateTime createDate = TEST_DATE;

    private OffsetDateTime lastUpdateDate = TEST_DATE;

    @Override
    public Order build() {
        return Order.builder()
                .id(id)
                .user(user)
                .giftCertificate(giftCertificate)
                .finalPrice(finalPrice)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
    }
}
