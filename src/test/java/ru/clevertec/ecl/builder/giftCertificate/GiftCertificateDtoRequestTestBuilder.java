package ru.clevertec.ecl.builder.giftCertificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.ecl.util.TestConstants.TEST_BIG_DECIMAL;
import static ru.clevertec.ecl.util.TestConstants.TEST_NUMBER;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGiftCertificateDtoRequest")
public class GiftCertificateDtoRequestTestBuilder implements TestBuilder<GiftCertificateDtoRequest> {

    private String name = TEST_STRING;

    private String description = TEST_STRING;

    private BigDecimal price = TEST_BIG_DECIMAL;

    private Long duration = TEST_NUMBER;

    private List<TagDtoRequest> tags = new ArrayList<>();

    @Override
    public GiftCertificateDtoRequest build() {
        return GiftCertificateDtoRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(tags)
                .build();
    }
}
