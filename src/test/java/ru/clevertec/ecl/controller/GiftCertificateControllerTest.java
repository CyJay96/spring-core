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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoResponseTestBuilder;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class GiftCertificateControllerTest {

    private GiftCertificateController giftCertificateController;

    @Mock
    private GiftCertificateService giftCertificateService;

    @Mock
    private PaginationProperties paginationProperties;

    @Captor
    ArgumentCaptor<GiftCertificateDtoRequest> giftCertificateDtoRequestCaptor;

    @BeforeEach
    void setUp() {
        giftCertificateController = new GiftCertificateController(giftCertificateService, paginationProperties);
    }

    @Test
    @DisplayName("Create Gift Certificate")
    void checkCreateGiftCertificateShouldReturnGiftCertificateDtoResponse() {
        GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
        GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();

        when(giftCertificateService.createGiftCertificate(giftCertificateDtoRequest)).thenReturn(giftCertificateDtoResponse);

        var giftCertificateDto = giftCertificateController.createGiftCertificate(giftCertificateDtoRequest);

        verify(giftCertificateService).createGiftCertificate(giftCertificateDtoRequestCaptor.capture());

        assertAll(
                () -> assertThat(giftCertificateDto.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(Objects.requireNonNull(giftCertificateDto.getBody()).getData()).isEqualTo(giftCertificateDtoResponse),
                () -> assertThat(giftCertificateDtoRequestCaptor.getValue()).isEqualTo(giftCertificateDtoRequest)
        );
    }

    @Test
    @DisplayName("Find all Gift Certificates")
    void checkFindAllGiftCertificatesShouldReturnGiftCertificateDtoResponsePage() {
        GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();

        PageResponse<GiftCertificateDtoResponse> pageResponse = PageResponse.<GiftCertificateDtoResponse>builder()
                .content(List.of(giftCertificateDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        when(giftCertificateService.getAllGiftCertificates(PAGE, PAGE_SIZE)).thenReturn(pageResponse);

        var giftCertificateDtoList = giftCertificateController.findAllGiftCertificates(PAGE, PAGE_SIZE);

        verify(giftCertificateService).getAllGiftCertificates(anyInt(), anyInt());

        assertAll(
                () -> assertThat(giftCertificateDtoList.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoList.getBody()).getData().getContent().get(0)).isEqualTo(giftCertificateDtoResponse)
        );
    }

    @Test
    @DisplayName("Find all Gift Certificates by criteria")
    void checkFindAllGiftCertificatesByCriteriaShouldReturnGiftCertificateDtoResponsePage() {
        GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();

        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder().build();

        PageResponse<GiftCertificateDtoResponse> pageResponse = PageResponse.<GiftCertificateDtoResponse>builder()
                .content(List.of(giftCertificateDtoResponse))
                .number(PAGE)
                .size(PAGE_SIZE)
                .numberOfElements(1)
                .build();

        when(giftCertificateService.getAllGiftCertificatesByCriteria(searchCriteria)).thenReturn(pageResponse);

        var giftCertificateDtoList = giftCertificateController.findAllGiftCertificatesByCriteria(searchCriteria, PAGE, PAGE_SIZE);

        verify(giftCertificateService).getAllGiftCertificatesByCriteria(any());

        assertAll(
                () -> assertThat(giftCertificateDtoList.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoList.getBody()).getData().getContent().get(0)).isEqualTo(giftCertificateDtoResponse)
        );
    }

    @Nested
    public class FindGiftCertificateByIdTest {
        @DisplayName("Find Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse()
                    .withId(id)
                    .build();

            when(giftCertificateService.getGiftCertificateById(id)).thenReturn(giftCertificateDtoResponse);

            var giftCertificateDto = giftCertificateController.findGiftCertificateById(id);

            verify(giftCertificateService).getGiftCertificateById(anyLong());

            assertAll(
                    () -> assertThat(giftCertificateDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(giftCertificateDto.getBody()).getData()).isEqualTo(giftCertificateDtoResponse)
            );
        }

        @Test
        @DisplayName("Find Gift Certificate by ID; not found")
        void checkFindGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateService).getGiftCertificateById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateController.findGiftCertificateById(TEST_ID));

            verify(giftCertificateService).getGiftCertificateById(anyLong());
        }
    }

    @Nested
    public class UpdateGiftCertificateByIdTest {
        @DisplayName("Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
            GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();

            when(giftCertificateService.updateGiftCertificateById(id, giftCertificateDtoRequest)).thenReturn(giftCertificateDtoResponse);

            var giftCertificateDto = giftCertificateController.updateGiftCertificateById(id, giftCertificateDtoRequest);

            verify(giftCertificateService).updateGiftCertificateById(anyLong(), giftCertificateDtoRequestCaptor.capture());

            assertAll(
                    () -> assertThat(giftCertificateDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(giftCertificateDto.getBody()).getData()).isEqualTo(giftCertificateDtoResponse),
                    () -> assertThat(giftCertificateDtoRequestCaptor.getValue()).isEqualTo(giftCertificateDtoRequest)
            );
        }

        @DisplayName("Partial Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkPartialUpdateGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
            GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();

            when(giftCertificateService.updateGiftCertificateByIdPartially(id, giftCertificateDtoRequest)).thenReturn(giftCertificateDtoResponse);

            var giftCertificateDto = giftCertificateController.updateGiftCertificateByIdPartially(id, giftCertificateDtoRequest);

            verify(giftCertificateService).updateGiftCertificateByIdPartially(anyLong(), giftCertificateDtoRequestCaptor.capture());

            assertAll(
                    () -> assertThat(giftCertificateDto.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(Objects.requireNonNull(giftCertificateDto.getBody()).getData()).isEqualTo(giftCertificateDtoResponse),
                    () -> assertThat(giftCertificateDtoRequestCaptor.getValue()).isEqualTo(giftCertificateDtoRequest)
            );
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkPartialUpdateGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();

            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateService).updateGiftCertificateByIdPartially(anyLong(), any());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateController.updateGiftCertificateByIdPartially(TEST_ID, giftCertificateDtoRequest));

            verify(giftCertificateService).updateGiftCertificateByIdPartially(anyLong(), any());
        }
    }

    @Nested
    public class DeleteGiftCertificateByIdTest {
        @DisplayName("Delete Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {4L, 5L, 6L})
        void checkDeleteGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            doNothing().when(giftCertificateService).deleteGiftCertificateById(id);

            var voidResponse = giftCertificateController.deleteGiftCertificateById(id);

            verify(giftCertificateService).deleteGiftCertificateById(anyLong());

            assertThat(voidResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("Delete Gift Certificate by ID; not found")
        void checkDeleteGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateService).deleteGiftCertificateById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateController.deleteGiftCertificateById(TEST_ID));

            verify(giftCertificateService).deleteGiftCertificateById(anyLong());
        }
    }
}
