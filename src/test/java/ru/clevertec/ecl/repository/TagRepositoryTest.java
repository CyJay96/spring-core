package ru.clevertec.ecl.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.config.DBConfig;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.impl.TagRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@ActiveProfiles("dev")
@ContextConfiguration(classes = DBConfig.class)
class TagRepositoryTest {

    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository = new TagRepositoryImpl();
    }

//    @Test
//    @DisplayName("Save Tag")
//    void checkSaveShouldReturnTag() {
//        Tag tag = TagTestBuilder.aTag()
//                .withId(1L)
//                .build();
//        Tag savedTag = tagRepository.save(tag);
//        assertThat(savedTag.getId()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Update Tag")
//    void checkUpdateShouldReturnTag() {
//        Tag tag = TagTestBuilder.aTag()
//                .withId(1L)
//                .withName("saved name")
//                .build();
//        tagRepository.save(tag);
//
//        tag.setName("updated name");
//        Tag updatedTag = tagRepository.update(tag);
//
//        assertAll(
//                () -> assertThat(updatedTag.getId()).isEqualTo(1),
//                () -> assertThat(updatedTag.getName()).isEqualTo("updated name")
//        );
//    }
//
//    @Test
//    @DisplayName("Find all Tags")
//    void checkFindAllShouldReturnTagList() {
//        List<Tag> tagList = tagRepository.findAll(PAGE, PAGE_SIZE);
//
//        assertThat(tagList.size()).isEqualTo(4);
//    }
//
//    @Test
//    @DisplayName("Find Tag by ID")
//    void checkFindByIdShouldReturnTagOptional() {
//        Optional<Tag> tag = tagRepository.findById(1L);
//        assertThat(tag.get().getId()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Delete Tag")
//    void checkDeleteByIdShouldReturnVoid() {
//        tagRepository.deleteById(1L);
//        List<Tag> tags = tagRepository.findAll(PAGE, PAGE_SIZE);
//        assertThat(tags.size()).isEqualTo(3);
//    }
}
