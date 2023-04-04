package ru.clevertec.ecl.builder.giftCertificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static ru.clevertec.ecl.util.TestConstants.TEST_BIG_DECIMAL;
import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_NUMBER;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGiftCertificate")
public class GiftCertificateTestBuilder implements TestBuilder<GiftCertificate> {

    private Long id = TEST_ID;

    private String name = TEST_STRING;

    private String description = TEST_STRING;

    private BigDecimal price = TEST_BIG_DECIMAL;

    private Duration duration = Duration.ofDays(TEST_NUMBER);

    private OffsetDateTime createDate = TEST_DATE;

    private OffsetDateTime lastUpdateDate = TEST_DATE;

    private List<Tag> tags =  Collections.emptyList();

    @Override
    public GiftCertificate build() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(id);
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        giftCertificate.setCreateDate(createDate);
        giftCertificate.setLastUpdateDate(lastUpdateDate);
        giftCertificate.setTags(tags);
        return giftCertificate;
    }
}
