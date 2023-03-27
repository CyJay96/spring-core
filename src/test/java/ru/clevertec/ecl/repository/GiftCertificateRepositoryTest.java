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
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.config.DBConfig;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.impl.GiftCertificateRepositoryImpl;
import ru.clevertec.ecl.repository.impl.TagRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.util.TestConstants.CREATE_DB;
import static ru.clevertec.ecl.util.TestConstants.INIT_DB;

@Transactional
@Rollback
@ActiveProfiles("dev")
@ContextConfiguration(classes = DBConfig.class)
class GiftCertificateRepositoryTest {

    private  GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(CREATE_DB)
                .addScript(INIT_DB)
                .build());

        TagRepository tagRepository = new TagRepositoryImpl(jdbcTemplate);
        giftCertificateRepository = new GiftCertificateRepositoryImpl(jdbcTemplate, tagRepository);
    }

    @Test
    void save() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate()
                .withId(1L)
                .build();
        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);
        assertThat(savedGiftCertificate.getId()).isEqualTo(1);
    }

    @Test
    void findAll() {
        List<GiftCertificate> all = giftCertificateRepository.findAll();
        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    void findAllByTagName() {
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.findAllByTagName("Java");
        assertThat(giftCertificateList.size()).isEqualTo(1);
    }

    @Test
    void findAllLikeDescription() {
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.findAllLikeDescription("frame");
        assertThat(giftCertificateList.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(1L);
        assertThat(giftCertificate.get().getId()).isEqualTo(1);
    }

    @Test
    void deleteById() {
        giftCertificateRepository.deleteById(1L);
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.findAll();
        assertThat(giftCertificateList.size()).isEqualTo(3);
    }
}
