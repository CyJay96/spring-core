package ru.clevertec.ecl.builder.giftCertificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;

import java.util.Collections;
import java.util.List;

import static ru.clevertec.ecl.util.TestConstants.TEST_NUMBER;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGiftCertificateCriteria")
public class GiftCertificateCriteriaTestBuilder implements TestBuilder<GiftCertificateCriteria> {

    private List<String> tagNames = Collections.emptyList();

    private String description = TEST_STRING;

    private Sort.Direction sortDirectionName = Sort.Direction.ASC;

    private Sort.Direction sortDirectionDate = Sort.Direction.ASC;

    private Integer offset = TEST_NUMBER.intValue();

    private Integer limit = TEST_NUMBER.intValue();

    @Override
    public GiftCertificateCriteria build() {
        GiftCertificateCriteria giftCertificateCriteria = new GiftCertificateCriteria();
        giftCertificateCriteria.setTagNames(tagNames);
        giftCertificateCriteria.setDescription(description);
        giftCertificateCriteria.setSortDirectionName(sortDirectionName);
        giftCertificateCriteria.setSortDirectionDate(sortDirectionDate);
        giftCertificateCriteria.setOffset(offset);
        giftCertificateCriteria.setLimit(limit);
        return giftCertificateCriteria;
    }
}
