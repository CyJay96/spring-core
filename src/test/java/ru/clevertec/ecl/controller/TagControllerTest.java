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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoResponseTestBuilder;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.TagNotFoundException;
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
class TagControllerTest {

    private TagController tagController;

    @Mock
    private TagService tagService;

    @Mock
    private PaginationProperties paginationProperties;

    @Captor
    ArgumentCaptor<TagDtoRequest> tagDtoRequestCaptor;

    @BeforeEach
    void setUp() {
        tagController = new TagController(tagService, paginationProperties);
    }

    @Test
    @DisplayName("Create Tag")
    void checkCreateTagShouldReturnTagDtoResponse() {
        TagDtoRequest giftCertificateDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
        TagDtoResponse giftCertificateDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();

        when(tagService.createTag(giftCertificateDtoRequest)).thenReturn(giftCertificateDtoResponse);

        var tagDto = tagController.createTag(giftCertificateDtoRequest);

        verify(tagService).createTag(tagDtoRequestCaptor.capture());

        assertAll(
                () -> assertThat(tagDto.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(Objects.requireNonNull(tagDto.getBody()).getData()).isEqualTo(giftCertificateDtoResponse),
                () -> assertThat(tagDtoRequestCaptor.getValue()).isEqualTo(giftCertificateDtoRequest)
        );
    }

    @Test
    @DisplayName("Find all Tags")
    void checkFindAllTagsShouldReturnTagPage() {
        TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();

        PageResponse<TagDtoResponse> pageResponse = PageResponse.<TagDtoResponse>builder()
                .content(List.of(tagDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        when(tagService.getAllTags(PAGE, PAGE_SIZE)).thenReturn(pageResponse);

        var tagDtoList = tagController.findAllTags(PAGE, PAGE_SIZE);

        verify(tagService).getAllTags(anyInt(), anyInt());

        assertAll(
                () -> assertThat(tagDtoList.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(tagDtoList.getBody()).getData().getContent().get(0)).isEqualTo(tagDtoResponse)
        );
    }

    @Nested
    public class FindTagByIdTest {
        @DisplayName("Find Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();

            when(tagService.getTagById(id)).thenReturn(tagDtoResponse);

            var tagDto = tagController.findTagById(id);

            verify(tagService).getTagById(anyLong());

            assertAll(
                    () -> assertThat(tagDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(tagDto.getBody()).getData()).isEqualTo(tagDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Tag by ID; not found")
        void checkFindTagByIdShouldThrowTagNotFoundException() {
            doThrow(TagNotFoundException.class).when(tagService).getTagById(anyLong());

            assertThrows(TagNotFoundException.class, () -> tagController.findTagById(TEST_ID));

            verify(tagService).getTagById(anyLong());
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

            when(tagService.updateTagById(id, tagDtoRequest)).thenReturn(tagDtoResponse);

            var tagDto = tagController.updateTagById(id, tagDtoRequest);

            verify(tagService).updateTagById(anyLong(), tagDtoRequestCaptor.capture());

            assertAll(
                    () -> assertThat(tagDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(tagDto.getBody()).getData()).isEqualTo(tagDtoResponse),
                    () -> assertThat(tagDtoRequestCaptor.getValue()).isEqualTo(tagDtoRequest)
            );
        }

        @DisplayName("Partial Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkPartialUpdateTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
            TagDtoResponse tagDtoResponse = TagDtoResponseTestBuilder.aTagDtoResponse().build();

            when(tagService.updateTagByIdPartially(id, tagDtoRequest)).thenReturn(tagDtoResponse);

            var tagDto = tagController.updateTagByIdPartially(id, tagDtoRequest);

            verify(tagService).updateTagByIdPartially(anyLong(), tagDtoRequestCaptor.capture());

            assertAll(
                    () -> assertThat(tagDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(tagDto.getBody()).getData()).isEqualTo(tagDtoResponse),
                    () -> assertThat(tagDtoRequestCaptor.getValue()).isEqualTo(tagDtoRequest)
            );
        }

        @Test
        @DisplayName("Partial Update Tag by ID; not found")
        void checkPartialUpdateTagByIdShouldThrowTagNotFoundException() {
            TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();

            doThrow(TagNotFoundException.class).when(tagService).updateTagByIdPartially(anyLong(), any());

            assertThrows(TagNotFoundException.class, () -> tagController.updateTagByIdPartially(TEST_ID, tagDtoRequest));

            verify(tagService).updateTagByIdPartially(anyLong(), any());
        }
    }

    @Nested
    public class DeleteTagByIdTest {
        @DisplayName("Delete Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {4L, 5L, 6L})
        void checkDeleteTagByIdShouldReturnTagDtoResponse(Long id) {
            doNothing().when(tagService).deleteTagById(id);

            var voidResponse = tagController.deleteTagById(id);

            verify(tagService).deleteTagById(anyLong());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete Tag by ID; not found")
        void checkDeleteTagByIdShouldThrowTagNotFoundException() {
            doThrow(TagNotFoundException.class).when(tagService).deleteTagById(anyLong());

            assertThrows(TagNotFoundException.class, () -> tagController.deleteTagById(TEST_ID));

            verify(tagService).deleteTagById(anyLong());
        }
    }
}
