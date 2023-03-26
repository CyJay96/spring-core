package ru.clevertec.ecl.builder.tag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.TestBuilder;
import ru.clevertec.ecl.model.entity.Tag;

import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTag")
public class TagTestBuilder implements TestBuilder<Tag> {

    private Long id = TEST_ID;

    private String name = TEST_STRING;

    @Override
    public Tag build() {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        return tag;
    }
}
