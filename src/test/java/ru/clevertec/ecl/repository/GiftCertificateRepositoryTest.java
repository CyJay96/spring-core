package ru.clevertec.ecl.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.config.DBConfig;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.impl.GiftCertificateRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@ActiveProfiles("dev")
@ContextConfiguration(classes = DBConfig.class)
class GiftCertificateRepositoryTest {

    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    void setUp() {
        giftCertificateRepository = new GiftCertificateRepositoryImpl();
    }

//    @Test
//    @DisplayName("Save Gift Certificate")
//    void checkSaveShouldReturnGiftCertificate() {
//        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate()
//                .withId(1L)
//                .build();
//        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);
//        assertThat(savedGiftCertificate.getId()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Update Gift Certificate")
//    void checkUpdateShouldReturnGiftCertificate() {
//        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate()
//                .withId(1L)
//                .withName("saved name")
//                .build();
//        giftCertificateRepository.save(giftCertificate);
//
//        giftCertificate.setName("updated name");
//        GiftCertificate updatedGiftCertificate = giftCertificateRepository.update(giftCertificate);
//
//        assertThat(updatedGiftCertificate.getId()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Find all Gift Certificates")
//    void checkFindAllShouldReturnGiftCertificateList() {
//        List<GiftCertificate> all = giftCertificateRepository.findAll(PAGE, PAGE_SIZE);
//        assertThat(all.size()).isEqualTo(4);
//    }
//
//    @Test
//    @DisplayName("Find Gift Certificate by ID")
//    void checkFindByIdShouldReturnGiftCertificateOptional() {
//        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(1L);
//        assertThat(giftCertificate.get().getId()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Delete Gift Certificate")
//    void checkDeleteByIdShouldReturnVoid() {
//        giftCertificateRepository.deleteById(1L);
//        List<GiftCertificate> giftCertificateList = giftCertificateRepository.findAll(PAGE, PAGE_SIZE);
//        assertThat(giftCertificateList.size()).isEqualTo(3);
//    }
}
