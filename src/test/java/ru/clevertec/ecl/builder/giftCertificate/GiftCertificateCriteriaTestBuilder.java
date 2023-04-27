package ru.clevertec.ecl.builder.giftCertificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;

import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGiftCertificateCriteria")
public class GiftCertificateCriteriaTestBuilder implements TestBuilder<GiftCertificateCriteria> {

    private List<String> tagNames = new ArrayList<>();

    private String description = TEST_STRING;

    private Sort.Direction sortDirectionName = Sort.Direction.ASC;

    private Sort.Direction sortDirectionDate = Sort.Direction.ASC;

    private Integer offset = PAGE;

    private Integer limit = PAGE_SIZE;

    @Override
    public GiftCertificateCriteria build() {
        return GiftCertificateCriteria.builder()
                .tagNames(tagNames)
                .description(description)
                .sortDirectionName(sortDirectionName)
                .sortDirectionDate(sortDirectionDate)
                .offset(offset)
                .limit(limit)
                .build();
    }
}
