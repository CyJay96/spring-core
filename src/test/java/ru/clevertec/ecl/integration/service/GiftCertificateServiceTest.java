package ru.clevertec.ecl.integration.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateCriteriaTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class GiftCertificateServiceTest extends BaseIntegrationTest {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final EntityManager entityManager;

    private final GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder
            .aGiftCertificateDtoRequest()
            .build();
    private final GiftCertificateCriteria searchCriteria = GiftCertificateCriteriaTestBuilder
            .aGiftCertificateCriteria()
            .withDescription("frame")
            .build();

    @Test
    @DisplayName("Save Gift Certificate")
    void checkSaveShouldReturnGiftCertificateDtoResponse() {
        GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                .save(giftCertificateDtoRequest);
        assertThat(actualGiftCertificate.getName()).isEqualTo(giftCertificateDtoRequest.getName());
    }

    @Test
    @DisplayName("Find all Gift Certificates")
    void checkFindAllShouldReturnGiftCertificateDtoResponsePage() {
        int expectedGiftCertificatesSize = (int) giftCertificateRepository.count();
        PageResponse<GiftCertificateDtoResponse> actualGiftCertificates = giftCertificateService
                .findAll(PAGE, PAGE_SIZE);
        assertThat(actualGiftCertificates.getContent()).hasSize(expectedGiftCertificatesSize);
    }

    @Test
    @DisplayName("Find all Gift Certificates by criteria")
    void checkFindAllByCriteriaShouldReturnGiftCertificateDtoResponsePage() {
        int expectedGiftCertificatesSize = 2;
        PageResponse<GiftCertificateDtoResponse> actualGiftCertificates = giftCertificateService
                .findAllByCriteria(searchCriteria, searchCriteria.getOffset(), searchCriteria.getLimit());
        assertThat(actualGiftCertificates.getContent()).hasSize(expectedGiftCertificatesSize);
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.findById(id);
            assertThat(actualGiftCertificate.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Find Gift Certificate by ID; not found")
        void checkFindByIdShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.findById(doesntExistGiftCertificateId)
            );
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .update(id, giftCertificateDtoRequest);
            assertThat(actualGiftCertificate.getId()).isEqualTo(id);
        }

        @DisplayName("Partial Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .updatePartially(id, giftCertificateDtoRequest);
            assertThat(actualGiftCertificate.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Update Gift Certificate by ID; not found")
        void checkUpdateShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService
                            .update(doesntExistGiftCertificateId, giftCertificateDtoRequest)
            );
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkUpdatePartiallyShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService
                            .updatePartially(doesntExistGiftCertificateId, giftCertificateDtoRequest)
            );
        }
    }

    @Nested
    public class AddTagToGiftCertificateTest {
        @Test
        @DisplayName("Add Tag to Gift Certificate")
        void checkAddTagToGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .addTagToGiftCertificate(existsGiftCertificateId, existsTagId);

            assertAll(
                    () -> assertThat(actualGiftCertificate.getId()).isEqualTo(existsGiftCertificateId),
                    () -> assertThat(actualGiftCertificate.getTags().stream()
                            .map(TagDtoResponse::getId)
                            .anyMatch(id -> id.equals(existsTagId))
                    ).isTrue()
            );
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Gift Certificate not found")
        void checkAddTagToGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            Long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();
            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.addTagToGiftCertificate(doesntExistGiftCertificateId, existsTagId)
            );
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag not found")
        void checkAddTagToGiftCertificateShouldThrowTagNotFoundException() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistTagId = new Random().nextLong(tagRepository
                    .findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.addTagToGiftCertificate(existsGiftCertificateId, doesntExistTagId)
            );
        }
    }

    @Nested
    public class DeleteTagFromGiftCertificateTest {
        @Test
        @DisplayName("Delete Tag from Gift Certificate")
        void checkDeleteTagFromGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .deleteTagFromGiftCertificate(existsGiftCertificateId, existsTagId);

            assertAll(
                    () -> assertThat(actualGiftCertificate.getId()).isEqualTo(existsGiftCertificateId),
                    () -> assertThat(actualGiftCertificate.getTags().stream()
                            .map(TagDtoResponse::getId)
                            .noneMatch(id -> id.equals(existsTagId))
                    ).isTrue()
            );
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Gift Certificate not found")
        void checkDeleteTagFromGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            Long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();
            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.deleteTagFromGiftCertificate(doesntExistGiftCertificateId, existsTagId)
            );
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Tag not found")
        void checkDeleteTagFromGiftCertificateShouldThrowTagNotFoundException() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.deleteTagFromGiftCertificate(existsGiftCertificateId, doesntExistTagId)
            );
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            giftCertificateService.deleteById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete Gift Certificate by ID; not found")
        void checkDeleteByIdShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(EntityNotFoundException.class, () -> {
                giftCertificateService.deleteById(doesntExistGiftCertificateId);
                entityManager.flush();
            });
        }
    }
}
