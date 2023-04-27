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
import ru.clevertec.ecl.exception.EntityNotFoundException;
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
    @DisplayName("Save Tag")
    void checkSaveShouldReturnTagDtoResponse() {
        TagDtoResponse actualTag = tagService.save(tagDtoRequest);
        assertThat(actualTag.getName()).isEqualTo(tagDtoRequest.getName());
    }

    @Test
    @DisplayName("Find all Tags")
    void checkFindAllShouldReturnTagDtoResponsePage() {
        int expectedTagsSize = (int) tagRepository.count();
        PageResponse<TagDtoResponse> actualTags = tagService.findAll(PAGE, PAGE_SIZE);
        assertThat(actualTags.getContent()).hasSize(expectedTagsSize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse actualTag = tagService.findById(id);
            assertThat(actualTag.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find Tag by ID; not found")
        void checkFindByIdShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> tagService.findById(doesntExistTagId));
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse actualTag = tagService.update(id, tagDtoRequest);
            assertThat(actualTag.getId()).isEqualTo(id);
        }

        @DisplayName("Partial Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnTagDtoResponse(Long id) {
            TagDtoResponse actualTag = tagService.updatePartially(id, tagDtoRequest);
            assertThat(actualTag.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update Tag by ID; not found")
        void checkUpdateShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> tagService.update(doesntExistTagId, tagDtoRequest));
        }

        @Test
        @DisplayName("Partial Update Tag by ID; not found")
        void checkUpdatePartiallyShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> tagService.updatePartially(doesntExistTagId, tagDtoRequest));
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            tagService.deleteById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete Tag by ID; not found")
        void checkDeleteByIdShouldThrowTagNotFoundException() {
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> {
                tagService.deleteById(doesntExistTagId);
                entityManager.flush();
            });
        }
    }
}
