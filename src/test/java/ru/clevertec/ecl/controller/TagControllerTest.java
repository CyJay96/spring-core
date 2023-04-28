package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoResponseTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.service.TagService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
class TagControllerTest {

    @InjectMocks
    private TagController tagController;

    @Mock
    private TagService tagService;

    @Captor
    ArgumentCaptor<TagDtoRequest> tagDtoRequestCaptor;

    private final TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
    private final TagDtoResponse expectedTagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();
    private final Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);

    @BeforeEach
    void setUp() {
        tagController = new TagController(tagService);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Tag")
        void checkSaveShouldReturnTagDtoResponse() {
            doReturn(expectedTagDtoResponse).when(tagService).save(tagDtoRequest);

            var actualTag = tagController.save(tagDtoRequest);

            verify(tagService).save(any());

            assertAll(
                    () -> assertThat(actualTag.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(actualTag.getBody()).getData()).isEqualTo(expectedTagDtoResponse)
            );
        }

        @Test
        @DisplayName("Save Tag with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnTagDtoResponse() {
            doReturn(expectedTagDtoResponse).when(tagService).save(tagDtoRequest);

            tagController.save(tagDtoRequest);

            verify(tagService).save(tagDtoRequestCaptor.capture());

            assertThat(tagDtoRequestCaptor.getValue()).isEqualTo(tagDtoRequest);
        }
    }

    @Test
    @DisplayName("Find all Tags")
    void checkFindAllShouldReturnTagPage() {
        PageResponse<TagDtoResponse> pageResponse = PageResponse.<TagDtoResponse>builder()
                .content(List.of(expectedTagDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(tagService).findAll(pageable);

        var actualTags = tagController.findAll(pageable);

        verify(tagService).findAll(any());

        assertAll(
                () -> assertThat(actualTags.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualTags.getBody()).getData().getContent().stream()
                        .anyMatch(actualTagDtoResponse -> actualTagDtoResponse.equals(expectedTagDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnTagDtoResponse(Long id) {
            doReturn(expectedTagDtoResponse).when(tagService).findById(id);

            var actualTag = tagController.findById(id);

            verify(tagService).findById(anyLong());

            assertAll(
                    () -> assertThat(actualTag.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualTag.getBody()).getData())
                            .isEqualTo(expectedTagDtoResponse)
            );
        }

        @Test
        @DisplayName("Find by ID; not found")
        void checkFindByIdShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(tagService).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> tagController.findById(TEST_ID));

            verify(tagService).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnTagDtoResponse(Long id) {
            doReturn(expectedTagDtoResponse).when(tagService).update(id, tagDtoRequest);

            var actualTag = tagController.update(id, tagDtoRequest);

            verify(tagService).update(anyLong(), any());

            assertAll(
                    () -> assertThat(actualTag.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualTag.getBody()).getData())
                            .isEqualTo(expectedTagDtoResponse)
            );
        }

        @DisplayName("Update Tag by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnTagDtoResponse(Long id) {
            doReturn(expectedTagDtoResponse).when(tagService).update(id, tagDtoRequest);

            tagController.update(id, tagDtoRequest);

            verify(tagService).update(anyLong(), tagDtoRequestCaptor.capture());

            assertThat(tagDtoRequestCaptor.getValue()).isEqualTo(tagDtoRequest);
        }

        @DisplayName("Partial Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnTagDtoResponse(Long id) {
            doReturn(expectedTagDtoResponse).when(tagService).updatePartially(id, tagDtoRequest);

            var actualTag = tagController.updatePartially(id, tagDtoRequest);

            verify(tagService).updatePartially(anyLong(), tagDtoRequestCaptor.capture());

            assertAll(
                    () -> assertThat(actualTag.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualTag.getBody()).getData())
                            .isEqualTo(expectedTagDtoResponse),
                    () -> assertThat(tagDtoRequestCaptor.getValue()).isEqualTo(tagDtoRequest)
            );
        }

        @DisplayName("Partial Update Tag by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyWithArgumentCaptorShouldReturnTagDtoResponse(Long id) {
            doReturn(expectedTagDtoResponse).when(tagService).updatePartially(id, tagDtoRequest);

            tagController.updatePartially(id, tagDtoRequest);

            verify(tagService).updatePartially(anyLong(), tagDtoRequestCaptor.capture());

            assertThat(tagDtoRequestCaptor.getValue()).isEqualTo(tagDtoRequest);
        }

        @Test
        @DisplayName("Update Tag by ID; not found")
        void checkUpdateShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(tagService).update(anyLong(), any());

            assertThrows(EntityNotFoundException.class, () -> tagController.update(TEST_ID, tagDtoRequest));

            verify(tagService).update(anyLong(), any());
        }

        @Test
        @DisplayName("Partial Update Tag by ID; not found")
        void checkUpdatePartiallyShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(tagService).updatePartially(anyLong(), any());

            assertThrows(EntityNotFoundException.class, () -> tagController.updatePartially(TEST_ID, tagDtoRequest));

            verify(tagService).updatePartially(anyLong(), any());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            doNothing().when(tagService).deleteById(id);

            var voidResponse = tagController.deleteById(id);

            verify(tagService).deleteById(anyLong());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete Tag by ID; not found")
        void checkDeleteByIdShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class).when(tagService).deleteById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> tagController.deleteById(TEST_ID));

            verify(tagService).deleteById(anyLong());
        }
    }
}
