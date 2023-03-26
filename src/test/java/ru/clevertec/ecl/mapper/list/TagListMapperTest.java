package ru.clevertec.ecl.mapper.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagListMapperTest {

    private TagListMapper tagListMapper;

    @Mock
    private TagMapper tagMapper;

    @Captor
    ArgumentCaptor<TagDtoRequest> tagDtoRequestCaptor;

    @Captor
    ArgumentCaptor<Tag> tagCaptor;

    @BeforeEach
    void setUp() {
        tagListMapper = new TagListMapperImpl(tagMapper);
    }

    @Test
    @DisplayName("Map Tag List DTO to Entity")
    void checkToEntityShouldReturnTagList() {
        Tag tag = TagTestBuilder.aTag().build();
        TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();

        when(tagMapper.toEntity(any())).thenReturn(tag);

        List<Tag> tagList = tagListMapper.toEntity(List.of(tagDtoRequest));

        verify(tagMapper).toEntity(tagDtoRequestCaptor.capture());

        assertAll(
                () -> assertThat(Objects.requireNonNull(tagList).size()).isEqualTo(1),
                () -> assertThat(Objects.requireNonNull(tagList).get(0)).isEqualTo(tag),
                () -> assertThat(Objects.requireNonNull(tagDtoRequestCaptor).getValue()).isEqualTo(tagDtoRequest)
        );
    }

    @Test
    @DisplayName("Map Tag List Entity to DTO")
    void checkToDtoShouldReturnTagDtoResponseList() {
        Tag tag = TagTestBuilder.aTag().build();
        TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();

        when(tagMapper.toDto(any())).thenReturn(tagDtoResponse);

        List<TagDtoResponse> tagDtoResponseList = tagListMapper.toDto(List.of(tag));

        verify(tagMapper).toDto(tagCaptor.capture());

        assertAll(
                () -> assertThat(Objects.requireNonNull(tagDtoResponseList).size()).isEqualTo(1),
                () -> assertThat(Objects.requireNonNull(tagDtoResponseList).get(0)).isEqualTo(tagDtoResponse),
                () -> assertThat(Objects.requireNonNull(tagCaptor).getValue()).isEqualTo(tag)
        );
    }
}
