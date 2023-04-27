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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.impl.TagServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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

    @Captor
    ArgumentCaptor<Tag> tagCaptor;

    private final TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
    private final TagDtoResponse expectedTagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
    private final Tag expectedTag = TagTestBuilder.aTag().build();

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository, tagMapper);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Tag")
        void checkSaveShouldReturnTagDtoResponse() {
            doReturn(expectedTag).when(tagRepository).save(expectedTag);
            doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);
            doReturn(expectedTag).when(tagMapper).toTag(tagDtoRequest);

            TagDtoResponse actualTag = tagService.save(tagDtoRequest);

            verify(tagRepository).save(any());
            verify(tagMapper).toTag(any());
            verify(tagMapper).toTagDtoResponse(any());

            assertThat(actualTag).isEqualTo(expectedTagDtoResponse);
        }

        @Test
        @DisplayName("Save Tag with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnTagDtoResponse() {
            doReturn(expectedTag).when(tagRepository).save(expectedTag);
            doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);
            doReturn(expectedTag).when(tagMapper).toTag(tagDtoRequest);

            tagService.save(tagDtoRequest);

            verify(tagRepository).save(tagCaptor.capture());
            verify(tagMapper).toTag(any());
            verify(tagMapper).toTagDtoResponse(any());

            assertThat(tagCaptor.getValue()).isEqualTo(expectedTag);
        }
    }

    @Test
    @DisplayName("Find all Tags")
    void checkFindAllShouldReturnTagDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedTag))).when(tagRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);

        PageResponse<TagDtoResponse> actualTags = tagService.findAll(PAGE, PAGE_SIZE);

        verify(tagRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        verify(tagMapper).toTagDtoResponse(any());

        assertThat(actualTags.getContent().stream()
                .anyMatch(actualTagDtoResponse -> actualTagDtoResponse.equals(expectedTagDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnTagDtoResponse(Long id) {
            doReturn(Optional.of(expectedTag)).when(tagRepository).findById(id);
            doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);

            TagDtoResponse actualTag = tagService.findById(id);

            verify(tagRepository).findById(anyLong());
            verify(tagMapper).toTagDtoResponse(any());

            assertThat(actualTag).isEqualTo(expectedTagDtoResponse);
        }

        @Test
        @DisplayName("Find Tag by ID; not found")
        void checkFindByIdShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> tagService.findById(TEST_ID));

            verify(tagRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnTagDtoResponse(Long id) {
            doReturn(Optional.of(expectedTag)).when(tagRepository).findById(id);
            doReturn(expectedTag).when(tagRepository).save(expectedTag);
            doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);

            TagDtoResponse actualTag = tagService.update(id, tagDtoRequest);

            verify(tagRepository).findById(anyLong());
            verify(tagRepository).save(any());
            verify(tagMapper).toTagDtoResponse(any());

            assertThat(actualTag).isEqualTo(expectedTagDtoResponse);
        }

        @DisplayName("Update Tag by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnTagDtoResponse(Long id) {
            doReturn(Optional.of(expectedTag)).when(tagRepository).findById(id);
            doReturn(expectedTag).when(tagRepository).save(expectedTag);
            doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);

            tagService.update(id, tagDtoRequest);

            verify(tagRepository).findById(anyLong());
            verify(tagRepository).save(tagCaptor.capture());
            verify(tagMapper).toTagDtoResponse(any());

            assertThat(tagCaptor.getValue()).isEqualTo(expectedTag);
        }

        @DisplayName("Partial Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnTagDtoResponse(Long id) {
            doReturn(Optional.of(expectedTag)).when(tagRepository).findById(id);
            doNothing().when(tagMapper).updateTag(tagDtoRequest, expectedTag);
            doReturn(expectedTag).when(tagRepository).save(expectedTag);
            doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);

            TagDtoResponse actualTag = tagService.updatePartially(id, tagDtoRequest);

            verify(tagRepository).findById(anyLong());
            verify(tagMapper).updateTag(any(), any());
            verify(tagRepository).save(any());
            verify(tagMapper).toTagDtoResponse(any());

            assertThat(actualTag).isEqualTo(expectedTagDtoResponse);
        }

        @DisplayName("Partial Update Tag by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyWithArgumentCaptorShouldReturnTagDtoResponse(Long id) {
            doReturn(Optional.of(expectedTag)).when(tagRepository).findById(id);
            doNothing().when(tagMapper).updateTag(tagDtoRequest, expectedTag);
            doReturn(expectedTag).when(tagRepository).save(expectedTag);
            doReturn(expectedTagDtoResponse).when(tagMapper).toTagDtoResponse(expectedTag);

            tagService.updatePartially(id, tagDtoRequest);

            verify(tagRepository).findById(anyLong());
            verify(tagMapper).updateTag(any(), any());
            verify(tagRepository).save(tagCaptor.capture());
            verify(tagMapper).toTagDtoResponse(any());

            assertThat(tagCaptor.getValue()).isEqualTo(expectedTag);
        }

        @Test
        @DisplayName("Update Tag by ID; not found")
        void checkUpdateShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> tagService.update(TEST_ID, tagDtoRequest));

            verify(tagRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Partial Update Tag by ID; not found")
        void checkUpdatePartiallyShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> tagService.updatePartially(TEST_ID, tagDtoRequest));

            verify(tagRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            doReturn(true).when(tagRepository).existsById(id);
            doNothing().when(tagRepository).deleteById(id);

            tagService.deleteById(id);

            verify(tagRepository).existsById(anyLong());
            verify(tagRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Delete Tag by ID; not found")
        void checkDeleteByIdShouldThrowTagNotFoundException() {
            doReturn(false).when(tagRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> tagService.deleteById(TEST_ID));

            verify(tagRepository).existsById(anyLong());
        }
    }
}
