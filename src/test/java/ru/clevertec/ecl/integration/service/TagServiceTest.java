package ru.clevertec.ecl.integration.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TagServiceTest extends BaseIntegrationTest {

    private final TagService tagService;
    private final TagRepository tagRepository;
    private final EntityManager entityManager;

    private final TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();

    @Test
    @DisplayName("Create Tag")
    void checkCreateTagShouldReturnTagDtoResponse() {
        Long expectedTagId = tagRepository.findFirstByOrderByIdDesc().get().getId() + 1;
        TagDtoResponse actualTag = tagService.createTag(tagDtoRequest);
        assertThat(actualTag.getId()).isEqualTo(expectedTagId);
    }

    @Test
    @DisplayName("Get all Tags")
    void checkGetAllTagsShouldReturnTagDtoResponsePage() {
        int expectedTagsSize = (int) tagRepository.count();
        PageResponse<TagDtoResponse> actualTags = tagService.getAllTags(PAGE, PAGE_SIZE);
        assertThat(actualTags.getContent()).hasSize(expectedTagsSize);
    }

    @Nested
    public class GetTagByIdTest {
        @DisplayName("Get Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse actualTag = tagService.getTagById(id);
            assertThat(actualTag.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Get Tag by ID; not found")
        void checkGetTagByIdShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(TagNotFoundException.class, () -> tagService.getTagById(doesntExistTagId));
        }
    }

    @Nested
    public class UpdateTagByIdTest {
        @DisplayName("Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse actualTag = tagService.updateTagById(id, tagDtoRequest);
            assertThat(actualTag.getId()).isEqualTo(id);
        }

        @DisplayName("Partial Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkPartialUpdateTagByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse actualTag = tagService.updateTagByIdPartially(id, tagDtoRequest);
            assertThat(actualTag.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update Tag by ID; not found")
        void checkUpdateTagByIdShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(TagNotFoundException.class, () -> tagService.updateTagById(doesntExistTagId, tagDtoRequest));
        }

        @Test
        @DisplayName("Partial Update Tag by ID; not found")
        void checkPartialUpdateTagByIdShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(TagNotFoundException.class, () -> tagService.updateTagByIdPartially(doesntExistTagId, tagDtoRequest));
        }
    }

    @Nested
    public class DeleteTagByIdTest {
        @DisplayName("Delete Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteTagByIdShouldReturnVoid(Long id) {
            tagService.deleteTagById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete Tag by ID; not found")
        void checkDeleteTagByIdShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(TagNotFoundException.class, () -> {
                tagService.deleteTagById(doesntExistTagId);
                entityManager.flush();
            });
        }
    }
}
