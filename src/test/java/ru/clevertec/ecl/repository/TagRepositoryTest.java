package ru.clevertec.ecl.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@DataJpaTest
@ActiveProfiles("dev")
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("Save Tag")
    void checkSaveShouldReturnTag() {
        Tag tag = TagTestBuilder.aTag()
                .withId(1L)
                .build();
        Tag savedTag = tagRepository.save(tag);
        assertThat(savedTag.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Update Tag")
    void checkUpdateShouldReturnTag() {
        Tag tag = TagTestBuilder.aTag()
                .withId(1L)
                .withName("saved name")
                .build();
        tagRepository.save(tag);

        tag.setName("updated name");
        Tag updatedTag = tagRepository.save(tag);

        assertAll(
                () -> assertThat(updatedTag.getId()).isEqualTo(1),
                () -> assertThat(updatedTag.getName()).isEqualTo("updated name")
        );
    }

    @Test
    @DisplayName("Find all Tags")
    void checkFindAllShouldReturnTagList() {
        Page<Tag> tagList = tagRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE));
        assertThat(tagList.getContent().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Find Tag by ID")
    void checkFindByIdShouldReturnTagOptional() {
        Optional<Tag> tag = tagRepository.findById(1L);
        assertThat(tag.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Delete Tag")
    void checkDeleteByIdShouldReturnVoid() {
        tagRepository.deleteById(1L);
        Page<Tag> tags = tagRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE));
        assertThat(tags.getContent().size()).isEqualTo(3);
    }
}
