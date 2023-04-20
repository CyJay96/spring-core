package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

class TagMapperTest {

    private TagMapper tagMapper;

    private final TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
    private final Tag tag = TagTestBuilder.aTag().build();

    @BeforeEach
    void setUp() {
        tagMapper = new TagMapperImpl();
    }

    @Test
    @DisplayName("Map Tag DTO to Entity")
    void checkToEntityShouldReturnTag() {
        Tag tag = tagMapper.toEntity(tagDtoRequest);

        assertAll(
                () -> assertThat(Objects.requireNonNull(tag).getId()).isNull(),
                () -> assertThat(Objects.requireNonNull(tag).getName()).isEqualTo(TEST_STRING)
        );
    }

    @Test
    @DisplayName("Map Tag Entity to DTO")
    void checkToDtoShouldReturnTagDtoResponse() {
        TagDtoResponse tagDtoResponse = tagMapper.toDto(tag);

        assertAll(
                () -> assertThat(Objects.requireNonNull(tagDtoResponse).getId()).isEqualTo(TEST_ID),
                () -> assertThat(Objects.requireNonNull(tagDtoResponse).getName()).isEqualTo(TEST_STRING)
        );
    }
}
