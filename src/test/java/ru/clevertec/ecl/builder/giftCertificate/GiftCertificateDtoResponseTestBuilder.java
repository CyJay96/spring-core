package ru.clevertec.ecl.builder.giftCertificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;

import java.math.BigDecimal;
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
@NoArgsConstructor(staticName = "aGiftCertificateDtoResponse")
public class GiftCertificateDtoResponseTestBuilder implements TestBuilder<GiftCertificateDtoResponse> {

    private Long id = TEST_ID;

    private String name = TEST_STRING;

    private String description = TEST_STRING;

    private BigDecimal price = TEST_BIG_DECIMAL;

    private Long duration = TEST_NUMBER;

    private OffsetDateTime createDate = TEST_DATE;

    private OffsetDateTime lastUpdateDate = TEST_DATE;

    private List<TagDtoResponse> tags =  Collections.emptyList();

    @Override
    public GiftCertificateDtoResponse build() {
        GiftCertificateDtoResponse giftCertificateDtoResponse = new GiftCertificateDtoResponse();
        giftCertificateDtoResponse.setId(id);
        giftCertificateDtoResponse.setName(name);
        giftCertificateDtoResponse.setDescription(description);
        giftCertificateDtoResponse.setPrice(price);
        giftCertificateDtoResponse.setDuration(duration);
        giftCertificateDtoResponse.setCreateDate(createDate);
        giftCertificateDtoResponse.setLastUpdateDate(lastUpdateDate);
        giftCertificateDtoResponse.setTags(tags);
        return giftCertificateDtoResponse;
    }
}
