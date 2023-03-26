package ru.clevertec.ecl.builder.tag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;

import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTagDtoRequest")
public class TagDtoRequestTestBuilder implements TestBuilder<TagDtoRequest> {

    private String name = TEST_STRING;

    @Override
    public TagDtoRequest build() {
        TagDtoRequest tagDtoRequest = new TagDtoRequest();
        tagDtoRequest.setName(name);
        return tagDtoRequest;
    }
}
