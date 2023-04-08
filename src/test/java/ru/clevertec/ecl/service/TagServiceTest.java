package ru.clevertec.ecl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.mapper.list.TagListMapper;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.impl.TagServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @Mock
    private TagListMapper tagListMapper;

    @Captor
    ArgumentCaptor<Tag> tagCaptor;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository, tagMapper, tagListMapper);
    }

    @Test
    @DisplayName("Create Tag")
    void checkCreateTagShouldReturnTagDtoResponse() {
        TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
        TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
        Tag tag = TagTestBuilder.aTag().build();

        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(tagDtoResponse);
        when(tagMapper.toEntity(tagDtoRequest)).thenReturn(tag);

        TagDtoResponse response = tagService.createTag(tagDtoRequest);

        verify(tagRepository).save(tagCaptor.capture());
        verify(tagMapper).toEntity(any());
        verify(tagMapper).toDto(any());

        assertAll(
                () -> assertThat(response).isEqualTo(tagDtoResponse),
                () -> assertThat(tagCaptor.getValue()).isEqualTo(tag)
        );
    }

    @Test
    @DisplayName("Get all Tags")
    void checkGetAllTagsShouldReturnTagDtoResponsePage() {
        TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
        Tag tag = TagTestBuilder.aTag().build();

        when(tagRepository.findAll(PAGE, PAGE_SIZE)).thenReturn(List.of(tag));
        when(tagListMapper.toDto(List.of(tag))).thenReturn(List.of(tagDtoResponse));

        PageResponse<TagDtoResponse> response = tagService.getAllTags(PAGE, PAGE_SIZE);

        verify(tagRepository).findAll(anyInt(), anyInt());
        verify(tagListMapper).toDto(any());

        assertThat(response.getContent().get(0)).isEqualTo(tagDtoResponse);
    }

    @Nested
    public class GetTagByIdTest {
        @DisplayName("Get Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
            Tag tag = TagTestBuilder.aTag().build();

            when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
            when(tagMapper.toDto(tag)).thenReturn(tagDtoResponse);

            TagDtoResponse response = tagService.getTagById(id);

            verify(tagRepository).findById(anyLong());
            verify(tagMapper).toDto(any());

            assertThat(response).isEqualTo(tagDtoResponse);
        }

        @Test
        @DisplayName("Get Tag by ID; not found")
        void checkGetTagByIdShouldThrowTagNotFoundException() {
            doThrow(TagNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(TagNotFoundException.class, () -> tagService.getTagById(TEST_ID));

            verify(tagRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTagByIdTest {
        @DisplayName("Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
            TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
            Tag tag = TagTestBuilder.aTag().build();

            when(tagRepository.update(tag)).thenReturn(tag);
            when(tagMapper.toEntity(tagDtoRequest)).thenReturn(tag);
            when(tagMapper.toDto(tag)).thenReturn(tagDtoResponse);

            TagDtoResponse response = tagService.updateTagById(id, tagDtoRequest);

            verify(tagRepository).update(tagCaptor.capture());
            verify(tagMapper).toEntity(any());
            verify(tagMapper).toDto(any());

            assertAll(
                    () -> assertThat(response).isEqualTo(tagDtoResponse),
                    () -> assertThat(tagCaptor.getValue()).isEqualTo(tag)
            );
        }

        @DisplayName("Partial Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkPartialUpdateTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
            TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
            Tag tag = TagTestBuilder.aTag().build();

            when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
            when(tagRepository.update(tag)).thenReturn(tag);
            when(tagMapper.toDto(tag)).thenReturn(tagDtoResponse);

            TagDtoResponse response = tagService.updateTagByIdPartially(id, tagDtoRequest);

            verify(tagRepository).findById(anyLong());
            verify(tagRepository).update(tagCaptor.capture());
            verify(tagMapper).toDto(any());

            assertAll(
                    () -> assertThat(response).isEqualTo(tagDtoResponse),
                    () -> assertThat(tagCaptor.getValue()).isEqualTo(tag)
            );
        }

        @Test
        @DisplayName("Partial Update Tag by ID; not found")
        void checkPartialUpdateTagByIdShouldThrowTagNotFoundException() {
            TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();

            doThrow(TagNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(TagNotFoundException.class, () -> tagService.updateTagByIdPartially(TEST_ID, tagDtoRequest));

            verify(tagRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteTagByIdTest {
        @DisplayName("Delete Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {4L, 5L, 6L})
        void checkDeleteTagByIdShouldReturnTagDtoResponse(Long id) {
            doNothing().when(tagRepository).deleteById(id);

            tagService.deleteTagById(id);

            verify(tagRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Delete Tag by ID; not found")
        void checkDeleteTagByIdShouldThrowTagNotFoundException() {
            doThrow(TagNotFoundException.class).when(tagRepository).deleteById(anyLong());

            assertThrows(TagNotFoundException.class, () -> tagService.deleteTagById(TEST_ID));

            verify(tagRepository).deleteById(anyLong());
        }
    }
}
