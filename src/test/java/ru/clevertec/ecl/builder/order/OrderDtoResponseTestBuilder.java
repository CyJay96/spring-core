package ru.clevertec.ecl.builder.order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.dto.response.OrderDtoResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static ru.clevertec.ecl.util.TestConstants.TEST_BIG_DECIMAL;
import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrderDtoResponse")
public class OrderDtoResponseTestBuilder implements TestBuilder<OrderDtoResponse> {

    private Long id = TEST_ID;

    private Long userId = TEST_ID;

    private Long giftCertificateId = TEST_ID;

    private BigDecimal finalPrice = TEST_BIG_DECIMAL;

    private OffsetDateTime createDate = TEST_DATE;

    private OffsetDateTime lastUpdateDate = TEST_DATE;

    @Override
    public OrderDtoResponse build() {
        OrderDtoResponse orderDtoResponse = new OrderDtoResponse();
        orderDtoResponse.setId(id);
        orderDtoResponse.setUserId(userId);
        orderDtoResponse.setGiftCertificateId(giftCertificateId);
        orderDtoResponse.setFinalPrice(finalPrice);
        orderDtoResponse.setCreateDate(createDate);
        orderDtoResponse.setLastUpdateDate(lastUpdateDate);
        return orderDtoResponse;
    }
}
