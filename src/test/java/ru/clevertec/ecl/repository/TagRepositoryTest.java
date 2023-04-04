package ru.clevertec.ecl.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.config.DBConfig;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.impl.TagRepositoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.util.TestConstants.CREATE_DB;
import static ru.clevertec.ecl.util.TestConstants.INIT_DB;

@Transactional
@Rollback
@ActiveProfiles("dev")
@ContextConfiguration(classes = DBConfig.class)
class TagRepositoryTest {

    private  TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(CREATE_DB)
                .addScript(INIT_DB)
                .build());

        tagRepository = new TagRepositoryImpl(jdbcTemplate);
    }

    @Test
    void save() {
        Tag tag = TagTestBuilder.aTag()
                .withId(1L)
                .build();
        Tag savedTag = tagRepository.save(tag);
        assertThat(savedTag.getId()).isEqualTo(1);
    }

    @Test
    void findAll() {
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList.size()).isEqualTo(4);
    }

    @Test
    void findAllById() {
        List<Tag> tags = tagRepository.findAllById(Collections.singleton(1L));
        assertThat(tags.size()).isEqualTo(1);
    }

    @Test
    void findAllByGiftCertificateId() {
        List<Tag> tags = tagRepository.findAllByGiftCertificateId(1L);
        assertThat(tags.size()).isEqualTo(3);
    }

    @Test
    void findById() {
        Optional<Tag> tag = tagRepository.findById(1L);
        assertThat(tag.get().getId()).isEqualTo(1);
    }

    @Test
    void findByName() {
        Optional<Tag> tag = tagRepository.findByName("Java");
        assertThat(tag.get().getName()).isEqualTo("Java");
    }

    @Test
    void deleteById() {
        tagRepository.deleteById(1L);
        List<Tag> tags = tagRepository.findAll();
        assertThat(tags.size()).isEqualTo(3);
    }

    @Test
    void deleteAllByGiftCertificateId() {
        tagRepository.deleteAllByGiftCertificateId(1L);
        List<Tag> tags = tagRepository.findAll();
        assertThat(tags.size()).isEqualTo(1);
    }
}
