package ru.clevertec.ecl.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@DataJpaTest
@ActiveProfiles("dev")
class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Test
    @DisplayName("Save Gift Certificate")
    void checkSaveShouldReturnGiftCertificate() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate()
                .withId(1L)
                .build();
        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);
        assertThat(savedGiftCertificate.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Update Gift Certificate")
    void checkUpdateShouldReturnGiftCertificate() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate()
                .withId(1L)
                .withName("saved name")
                .build();
        giftCertificateRepository.save(giftCertificate);

        giftCertificate.setName("updated name");
        GiftCertificate updatedGiftCertificate = giftCertificateRepository.save(giftCertificate);

        assertThat(updatedGiftCertificate.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Find all Gift Certificates")
    void checkFindAllShouldReturnGiftCertificateList() {
        Page<GiftCertificate> all = giftCertificateRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE));
        assertThat(all.getContent().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Find Gift Certificate by ID")
    void checkFindByIdShouldReturnGiftCertificateOptional() {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(1L);
        assertThat(giftCertificate.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Delete Gift Certificate")
    void checkDeleteByIdShouldReturnVoid() {
        giftCertificateRepository.deleteById(1L);
        Page<GiftCertificate> giftCertificateList = giftCertificateRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE));
        assertThat(giftCertificateList.getContent().size()).isEqualTo(3);
    }
}
