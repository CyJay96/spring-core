package ru.clevertec.ecl.builder.tag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;

import java.util.Collections;
import java.util.List;

import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTagDtoResponse")
public class TagDtoResponseTestBuilder implements TestBuilder<TagDtoResponse> {

    private Long id = TEST_ID;

    private String name = TEST_STRING;

    private List<Long> giftCertificatesIds = Collections.emptyList();

    @Override
    public TagDtoResponse build() {
        TagDtoResponse tagDtoResponse = new TagDtoResponse();
        tagDtoResponse.setId(id);
        tagDtoResponse.setName(name);
        tagDtoResponse.setGiftCertificatesIds(giftCertificatesIds);
        return tagDtoResponse;
    }
}
