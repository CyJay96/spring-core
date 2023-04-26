package ru.clevertec.ecl.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GiftCertificateRepositoryTest extends BaseIntegrationTest {

    private final GiftCertificateRepository giftCertificateRepository;

    @Nested
    public class FindFirstByOrderByIdAscTest {
        @Test
        @DisplayName("Find first Gift Certificate order by ID ASC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyGiftCertificateOptional() {
            List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll();

            GiftCertificate expectedGiftCertificate = giftCertificates.stream()
                    .sorted(Comparator.comparing(GiftCertificate::getId))
                    .limit(1)
                    .toList()
                    .get(0);

            GiftCertificate actualGiftCertificate = giftCertificateRepository.findFirstByOrderByIdAsc().get();

            assertThat(actualGiftCertificate.getId()).isEqualTo(expectedGiftCertificate.getId());
        }

        @Test
        @DisplayName("Find first Gift Certificate order by ID ASC; not found")
        void checkFindFirstByOrderByIdAscShouldReturnGiftCertificateOptional() {
            giftCertificateRepository.deleteAll();
            Optional<GiftCertificate> actualGiftCertificateOptional = giftCertificateRepository.findFirstByOrderByIdAsc();
            assertThat(actualGiftCertificateOptional.isEmpty()).isTrue();
        }
    }

    @Nested
    public class FindFirstByOrderByIdDescTest {
        @Test
        @DisplayName("Find first Gift Certificate order by ID DESC")
        void checkFindFirstByOrderByIdAscShouldReturnNotEmptyGiftCertificateOptional() {
            List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll();

            GiftCertificate expectedGiftCertificate = giftCertificates.stream()
                    .sorted(Comparator.comparing(GiftCertificate::getId).reversed())
                    .limit(1)
                    .toList()
                    .get(0);

            GiftCertificate actualGiftCertificate = giftCertificateRepository.findFirstByOrderByIdDesc().get();

            assertThat(actualGiftCertificate.getId()).isEqualTo(expectedGiftCertificate.getId());
        }

        @Test
        @DisplayName("Find first GiftCertificate order by ID DESC; not found")
        void checkFindFirstByOrderByIdDescShouldReturnEmptyGiftCertificateOptional() {
            giftCertificateRepository.deleteAll();
            Optional<GiftCertificate> actualGiftCertificateOptional = giftCertificateRepository.findFirstByOrderByIdDesc();
            assertThat(actualGiftCertificateOptional.isEmpty()).isTrue();
        }
    }
}
