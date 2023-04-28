package ru.clevertec.ecl.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TagRepositoryTest extends BaseIntegrationTest {

    private final TagRepository tagRepository;

    @Nested
    public class FindFirstByOrderByIdAscTest {
        @Test
        @DisplayName("Find first Tag order by ID ASC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyTagOptional() {
            List<Tag> tags = tagRepository.findAll();

            Tag expectedTag = tags.stream()
                    .sorted(Comparator.comparing(Tag::getId))
                    .limit(1)
                    .toList()
                    .get(0);

            Tag actualTag = tagRepository.findFirstByOrderByIdAsc().get();

            assertThat(actualTag.getId()).isEqualTo(expectedTag.getId());
        }

        @Test
        @DisplayName("Find first Tag order by ID ASC; not found")
        void checkFindFirstByOrderByIdAscShouldReturnTagOptional() {
            tagRepository.deleteAll();
            Optional<Tag> actualTagOptional = tagRepository.findFirstByOrderByIdAsc();
            assertThat(actualTagOptional.isEmpty()).isTrue();
        }
    }

    @Nested
    public class FindFirstByOrderByIdDescTest {
        @Test
        @DisplayName("Find first Order order by ID DESC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyOrderOptional() {
            List<Tag> tags = tagRepository.findAll();

            Tag expectedTag = tags.stream()
                    .sorted(Comparator.comparing(Tag::getId).reversed())
                    .limit(1)
                    .toList()
                    .get(0);

            Tag actualTag = tagRepository.findFirstByOrderByIdDesc().get();

            assertThat(actualTag.getId()).isEqualTo(expectedTag.getId());
        }

        @Test
        @DisplayName("Find first Tag order by ID DESC; not found")
        void checkFindFirstByOrderByIdDescShouldReturnEmptyTagOptional() {
            tagRepository.deleteAll();
            Optional<Tag> actualTagOptional = tagRepository.findFirstByOrderByIdDesc();
            assertThat(actualTagOptional.isEmpty()).isTrue();
        }
    }
}
