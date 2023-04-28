package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateCriteriaTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoResponseTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class GiftCertificateControllerTest {

    @InjectMocks
    private GiftCertificateController giftCertificateController;

    @Mock
    private GiftCertificateService giftCertificateService;

    @Captor
    ArgumentCaptor<GiftCertificateDtoRequest> giftCertificateDtoRequestCaptor;

    private final GiftCertificateDtoRequest expectedGiftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder
            .aGiftCertificateDtoRequest()
            .build();
    private final GiftCertificateDtoResponse expectedGiftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder
            .aGiftCertificateDtoResponse()
            .build();
    private final GiftCertificateCriteria searchCriteria = GiftCertificateCriteriaTestBuilder
            .aGiftCertificateCriteria()
            .build();
    private final Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);

    @BeforeEach
    void setUp() {
        giftCertificateController = new GiftCertificateController(giftCertificateService);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Gift Certificate")
        void checkSaveShouldReturnGiftCertificateDtoResponse() {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).save(expectedGiftCertificateDtoRequest);

            var actualGiftCertificate = giftCertificateController.save(expectedGiftCertificateDtoRequest);

            verify(giftCertificateService).save(any());

            assertAll(
                    () -> assertThat(actualGiftCertificate.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(Objects.requireNonNull(actualGiftCertificate.getBody()).getData())
                            .isEqualTo(expectedGiftCertificateDtoResponse)
            );
        }

        @Test
        @DisplayName("Save Gift Certificate with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnGiftCertificateDtoResponse() {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).save(expectedGiftCertificateDtoRequest);

            giftCertificateController.save(expectedGiftCertificateDtoRequest);

            verify(giftCertificateService).save(giftCertificateDtoRequestCaptor.capture());

            assertThat(giftCertificateDtoRequestCaptor.getValue()).isEqualTo(expectedGiftCertificateDtoRequest);
        }
    }

    @Test
    @DisplayName("Find all Gift Certificates")
    void checkFindAllShouldReturnGiftCertificateDtoResponsePage() {
        PageResponse<GiftCertificateDtoResponse> pageResponse = PageResponse.<GiftCertificateDtoResponse>builder()
                .content(List.of(expectedGiftCertificateDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(giftCertificateService).findAll(pageable);

        var actualGiftCertificates = giftCertificateController.findAll(pageable);

        verify(giftCertificateService).findAll(any());

        assertAll(
                () -> assertThat(actualGiftCertificates.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualGiftCertificates.getBody()).getData().getContent().stream()
                        .anyMatch(actualGiftCertificateDtoResponse ->
                                actualGiftCertificateDtoResponse.equals(expectedGiftCertificateDtoResponse))
                ).isTrue()
        );
    }

    @Test
    @DisplayName("Find all Gift Certificates by criteria")
    void checkFindAllByCriteriaShouldReturnGiftCertificateDtoResponsePage() {
        PageResponse<GiftCertificateDtoResponse> pageResponse = PageResponse.<GiftCertificateDtoResponse>builder()
                .content(List.of(expectedGiftCertificateDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        doReturn(pageResponse).when(giftCertificateService).findAllByCriteria(searchCriteria, pageable);

        var actualGiftCertificates = giftCertificateController.findAllByCriteria(searchCriteria, pageable);

        verify(giftCertificateService).findAllByCriteria(any(), any());

        assertAll(
                () -> assertThat(actualGiftCertificates.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(actualGiftCertificates.getBody()).getData().getContent().stream()
                        .anyMatch(actualGiftCertificateDtoResponse ->
                                actualGiftCertificateDtoResponse.equals(expectedGiftCertificateDtoResponse))
                ).isTrue()
        );
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse expectedGiftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder
                    .aGiftCertificateDtoResponse()
                    .withId(id)
                    .build();

            doReturn(expectedGiftCertificateDtoResponse).when(giftCertificateService).findById(id);

            var actualGiftCertificate = giftCertificateController.findById(id);

            verify(giftCertificateService).findById(anyLong());

            assertAll(
                    () -> assertThat(actualGiftCertificate.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualGiftCertificate.getBody()).getData())
                            .isEqualTo(expectedGiftCertificateDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Gift Certificate by ID; not found")
        void checkFindByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateService).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> giftCertificateController.findById(TEST_ID));

            verify(giftCertificateService).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).update(id, expectedGiftCertificateDtoRequest);

            var actualGiftCertificate = giftCertificateController.update(id, expectedGiftCertificateDtoRequest);

            verify(giftCertificateService).update(anyLong(), any());

            assertAll(
                    () -> assertThat(actualGiftCertificate.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualGiftCertificate.getBody()).getData())
                            .isEqualTo(expectedGiftCertificateDtoResponse)
            );
        }

        @DisplayName("Update Gift Certificate by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).update(id, expectedGiftCertificateDtoRequest);

            giftCertificateController.update(id, expectedGiftCertificateDtoRequest);

            verify(giftCertificateService).update(anyLong(), giftCertificateDtoRequestCaptor.capture());

            assertThat(giftCertificateDtoRequestCaptor.getValue())
                    .isEqualTo(expectedGiftCertificateDtoRequest);
        }

        @DisplayName("Partial Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).updatePartially(id, expectedGiftCertificateDtoRequest);

            var giftCertificateDto = giftCertificateController.updatePartially(id, expectedGiftCertificateDtoRequest);

            verify(giftCertificateService).updatePartially(anyLong(), any());

            assertAll(
                    () -> assertThat(giftCertificateDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(giftCertificateDto.getBody()).getData())
                            .isEqualTo(expectedGiftCertificateDtoResponse)
            );
        }

        @DisplayName("Partial Update Gift Certificate by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyWithArgumentCaptorShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).updatePartially(id, expectedGiftCertificateDtoRequest);

            giftCertificateController.updatePartially(id, expectedGiftCertificateDtoRequest);

            verify(giftCertificateService).updatePartially(anyLong(), giftCertificateDtoRequestCaptor.capture());

            assertThat(giftCertificateDtoRequestCaptor.getValue()).isEqualTo(expectedGiftCertificateDtoRequest);
        }

        @Test
        @DisplayName("Update Gift Certificate by ID; not found")
        void checkUpdateShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateService).update(anyLong(), any());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateController.update(TEST_ID, expectedGiftCertificateDtoRequest)
            );

            verify(giftCertificateService).update(anyLong(), any());
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkUpdatePartiallyShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateService).updatePartially(anyLong(), any());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateController.updatePartially(TEST_ID, expectedGiftCertificateDtoRequest)
            );

            verify(giftCertificateService).updatePartially(anyLong(), any());
        }
    }

    @Nested
    public class AddTagToGiftCertificateTest {
        @Test
        @DisplayName("Add Tag to Gift Certificate")
        void checkAddTagToGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).addTagToGiftCertificate(TEST_ID, TEST_ID);

            var actualGiftCertificate = giftCertificateController.addTagToGiftCertificate(TEST_ID, TEST_ID);

            verify(giftCertificateService).addTagToGiftCertificate(anyLong(), anyLong());

            assertAll(
                    () -> assertThat(actualGiftCertificate.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualGiftCertificate.getBody()).getData())
                            .isEqualTo(expectedGiftCertificateDtoResponse)
            );
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Gift Certificate not found")
        void checkAddTagToGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class)
                    .when(giftCertificateService).addTagToGiftCertificate(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.addTagToGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(giftCertificateService).addTagToGiftCertificate(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag not found")
        void checkAddTagToGiftCertificateShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class)
                    .when(giftCertificateService).addTagToGiftCertificate(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.addTagToGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(giftCertificateService).addTagToGiftCertificate(anyLong(), anyLong());
        }
    }

    @Nested
    public class DeleteTagFromGiftCertificateTest {
        @Test
        @DisplayName("Delete Tag from Gift Certificate")
        void checkDeleteTagFromGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateService).deleteTagFromGiftCertificate(TEST_ID, TEST_ID);

            var actualGiftCertificate = giftCertificateController.deleteTagFromGiftCertificate(TEST_ID, TEST_ID);

            verify(giftCertificateService).deleteTagFromGiftCertificate(anyLong(), anyLong());

            assertAll(
                    () -> assertThat(actualGiftCertificate.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(actualGiftCertificate.getBody()).getData())
                            .isEqualTo(expectedGiftCertificateDtoResponse)
            );
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Gift Certificate not found")
        void checkDeleteTagFromGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class)
                    .when(giftCertificateService).deleteTagFromGiftCertificate(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.deleteTagFromGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(giftCertificateService).deleteTagFromGiftCertificate(anyLong(), anyLong());
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Tag not found")
        void checkDeleteTagFromGiftCertificateShouldThrowTagNotFoundException() {
            doThrow(EntityNotFoundException.class)
                    .when(giftCertificateService).deleteTagFromGiftCertificate(anyLong(), anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.deleteTagFromGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(giftCertificateService).deleteTagFromGiftCertificate(anyLong(), anyLong());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) {
            doNothing().when(giftCertificateService).deleteById(id);

            var voidResponse = giftCertificateController.deleteById(id);

            verify(giftCertificateService).deleteById(anyLong());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete Gift Certificate by ID; not found")
        void checkDeleteByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateService).deleteById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> giftCertificateController.deleteById(TEST_ID));

            verify(giftCertificateService).deleteById(anyLong());
        }
    }
}
