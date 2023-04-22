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
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.TagNotFoundException;
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
public class GiftCertificateServiceTest extends BaseIntegrationTest {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final EntityManager entityManager;

    private final GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
    private final GiftCertificateCriteria searchCriteria = GiftCertificateCriteriaTestBuilder.aGiftCertificateCriteria()
            .withDescription("frame")
            .build();

    @Test
    @DisplayName("Create Gift Certificate")
    void checkCreateGiftCertificateShouldReturnGiftCertificateDtoResponse() {
        Long expectedGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1;
        GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.createGiftCertificate(giftCertificateDtoRequest);
        assertThat(actualGiftCertificate.getId()).isEqualTo(expectedGiftCertificateId);
    }

    @Test
    @DisplayName("Get all Gift Certificates")
    void checkGetAllGiftCertificatesShouldReturnGiftCertificateDtoResponsePage() {
        int expectedGiftCertificatesSize = (int) giftCertificateRepository.count();
        PageResponse<GiftCertificateDtoResponse> actualGiftCertificates = giftCertificateService.getAllGiftCertificates(PAGE, PAGE_SIZE);
        assertThat(actualGiftCertificates.getContent()).hasSize(expectedGiftCertificatesSize);
    }

    @Test
    @DisplayName("Get all Gift Certificates by criteria")
    void checkGetAllGiftCertificatesByCriteriaShouldReturnGiftCertificateDtoResponsePage() {
        int expectedSize = 2;
        PageResponse<GiftCertificateDtoResponse> actualGiftCertificates = giftCertificateService.getAllGiftCertificatesByCriteria(searchCriteria);
        assertThat(actualGiftCertificates.getContent()).hasSize(expectedSize);
    }

    @Nested
    public class GetGiftCertificateByIdTest {
        @DisplayName("Get Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.getGiftCertificateById(id);
            assertThat(actualGiftCertificate.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Get Gift Certificate by ID; not found")
        void checkGetGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.getGiftCertificateById(doesntExistGiftCertificateId));
        }
    }

    @Nested
    public class UpdateGiftCertificateByIdTest {
        @DisplayName("Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.updateGiftCertificateById(id, giftCertificateDtoRequest);
            assertThat(actualGiftCertificate.getId()).isEqualTo(id);
        }

        @DisplayName("Partial Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkPartialUpdateGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.updateGiftCertificateByIdPartially(id, giftCertificateDtoRequest);
            assertThat(actualGiftCertificate.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkPartialUpdateGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.updateGiftCertificateByIdPartially(doesntExistGiftCertificateId, giftCertificateDtoRequest));
        }
    }

    @Nested
    public class AddTagToGiftCertificateTest {
        @Test
        @DisplayName("Add Tag to Gift Certificate")
        void checkAddTagToGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.addTagToGiftCertificate(existsGiftCertificateId, existsTagId);

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
            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.addTagToGiftCertificate(doesntExistGiftCertificateId, existsTagId));
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag not found")
        void checkAddTagToGiftCertificateShouldThrowTagNotFoundException() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistTagId = new Random().nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(TagNotFoundException.class, () -> giftCertificateService.addTagToGiftCertificate(existsGiftCertificateId, doesntExistTagId));
        }
    }

    @Nested
    public class DeleteTagFromGiftCertificateTest {
        @Test
        @DisplayName("Delete Tag from Gift Certificate")
        void checkDeleteTagFromGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.deleteTagFromGiftCertificate(existsGiftCertificateId, existsTagId);

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
            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.deleteTagFromGiftCertificate(doesntExistGiftCertificateId, existsTagId));
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Tag not found")
        void checkDeleteTagFromGiftCertificateShouldThrowTagNotFoundException() {
            Long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            Long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(TagNotFoundException.class, () -> giftCertificateService.deleteTagFromGiftCertificate(existsGiftCertificateId, doesntExistTagId));
        }
    }

    @Nested
    public class DeleteGiftCertificateByIdTest {
        @DisplayName("Delete Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            giftCertificateService.deleteGiftCertificateById(id);
            entityManager.flush();
        }

        @Test
        @DisplayName("Delete Gift Certificate by ID; not found")
        void checkDeleteGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            Long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            assertThrows(GiftCertificateNotFoundException.class, () -> {
                giftCertificateService.deleteGiftCertificateById(doesntExistGiftCertificateId);
                entityManager.flush();
            });
        }
    }
}
