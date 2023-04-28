package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

class TagMapperTest {

    private TagMapper tagMapper;

    private final TagDtoRequest expectedTagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
    private final TagDtoResponse expectedTagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
    private final Tag expectedTag = TagTestBuilder.aTag().build();

    @BeforeEach
    void setUp() {
        tagMapper = Mappers.getMapper(TagMapper.class);
    }

    @Test
    @DisplayName("Map Tag DTO to Entity")
    void checkToTagShouldReturnTag() {
        Tag actualTag = tagMapper.toTag(expectedTagDtoRequest);
        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    @DisplayName("Map Tag Entity to DTO")
    void checkToTagDtoResponseShouldReturnTagDtoResponse() {
        TagDtoResponse actualTagDtoResponse = tagMapper.toTagDtoResponse(expectedTag);
        assertThat(actualTagDtoResponse).isEqualTo(expectedTagDtoResponse);
        assertAll(
                () -> assertThat(Objects.requireNonNull(actualTagDtoResponse).getId()).isEqualTo(TEST_ID),
                () -> assertThat(Objects.requireNonNull(actualTagDtoResponse).getName()).isEqualTo(TEST_STRING)
        );
    }

    @Test
    @DisplayName("Map Tag List DTO to Entity")
    void checkToTagListShouldReturnTagList() {
        List<Tag> actualTagList = tagMapper.toTagList(List.of(expectedTagDtoRequest));
        assertAll(
                () -> assertThat(Objects.requireNonNull(actualTagList).size()).isEqualTo(1),
                () -> assertThat(Objects.requireNonNull(actualTagList).stream()
                        .anyMatch(actualTag -> actualTag.equals(expectedTag))
                ).isTrue()
        );
    }
}
