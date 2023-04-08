package ru.clevertec.ecl.service;

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
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.list.GiftCertificateListMapper;
import ru.clevertec.ecl.mapper.list.TagListMapper;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.impl.GiftCertificateServiceImpl;
import ru.clevertec.ecl.service.searcher.GiftCertificateSearcher;

import java.util.List;
import java.util.Optional;

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
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    private GiftCertificateService giftCertificateService;

    @Mock
    private GiftCertificateSearcher giftCertificateSearcher;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private GiftCertificateMapper giftCertificateMapper;

    @Mock
    private GiftCertificateListMapper giftCertificateListMapper;

    @Mock
    private TagListMapper tagListMapper;

    @Captor
    ArgumentCaptor<GiftCertificate> giftCertificateCaptor;

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateSearcher, giftCertificateRepository,
                giftCertificateMapper, giftCertificateListMapper, tagListMapper);
    }

    @Test
    @DisplayName("Create Gift Certificate")
    void checkCreateGiftCertificateShouldReturnGiftCertificateDtoResponse() {
        GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
        GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
        when(giftCertificateMapper.toEntity(giftCertificateDtoRequest)).thenReturn(giftCertificate);
        when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

        GiftCertificateDtoResponse response = giftCertificateService.createGiftCertificate(giftCertificateDtoRequest);

        verify(giftCertificateRepository).save(giftCertificateCaptor.capture());
        verify(giftCertificateMapper).toEntity(any());
        verify(giftCertificateMapper).toDto(any());

        assertAll(
                () -> assertThat(response).isEqualTo(giftCertificateDtoResponse),
                () -> assertThat(giftCertificateCaptor.getValue()).isEqualTo(giftCertificate)
        );
    }

    @Test
    @DisplayName("Get all Gift Certificates")
    void checkGetAllGiftCertificatesShouldReturnGiftCertificateDtoResponsePage() {
        GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

        when(giftCertificateRepository.findAll(PAGE, PAGE_SIZE)).thenReturn(List.of(giftCertificate));
        when(giftCertificateListMapper.toDto(List.of(giftCertificate))).thenReturn(List.of(giftCertificateDtoResponse));

        PageResponse<GiftCertificateDtoResponse> response = giftCertificateService.getAllGiftCertificates(PAGE, PAGE_SIZE);

        verify(giftCertificateRepository).findAll(anyInt(), anyInt());
        verify(giftCertificateListMapper).toDto(any());

        assertThat(response.getContent().get(0)).isEqualTo(giftCertificateDtoResponse);
    }

    @Test
    @DisplayName("Get all Gift Certificates by criteria")
    void checkGetAllGiftCertificatesByCriteriaShouldReturnGiftCertificateDtoResponsePage() {
        GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder()
                .tagName(TEST_STRING)
                .build();

        when(giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria, PAGE, PAGE_SIZE)).thenReturn(List.of(giftCertificate));
        when(giftCertificateListMapper.toDto(List.of(giftCertificate))).thenReturn(List.of(giftCertificateDtoResponse));

        PageResponse<GiftCertificateDtoResponse> response = giftCertificateService.getAllGiftCertificatesByCriteria(searchCriteria, PAGE, PAGE_SIZE);

        verify(giftCertificateSearcher).getGiftCertificatesByCriteria(any(), anyInt(), anyInt());
        verify(giftCertificateListMapper).toDto(any());

        assertThat(response.getContent().get(0)).isEqualTo(giftCertificateDtoResponse);
    }

    @Nested
    public class GetProductByIdTest {
        @DisplayName("Get Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

            when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
            when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

            GiftCertificateDtoResponse response = giftCertificateService.getGiftCertificateById(id);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateMapper).toDto(any());

            assertThat(response).isEqualTo(giftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Get Gift Certificate by ID; not found")
        void checkGetGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.getGiftCertificateById(TEST_ID));

            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateProductByIdTest {
        @DisplayName("Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
            GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

            when(giftCertificateRepository.update(giftCertificate)).thenReturn(giftCertificate);
            when(giftCertificateMapper.toEntity(giftCertificateDtoRequest)).thenReturn(giftCertificate);
            when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

            GiftCertificateDtoResponse response = giftCertificateService.updateGiftCertificateById(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).update(giftCertificateCaptor.capture());
            verify(giftCertificateMapper).toEntity(any());
            verify(giftCertificateMapper).toDto(any());

            assertAll(
                    () -> assertThat(response).isEqualTo(giftCertificateDtoResponse),
                    () -> assertThat(giftCertificateCaptor.getValue()).isEqualTo(giftCertificate)
            );
        }

        @DisplayName("Partial Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkPartialUpdateGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
            GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

            when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
            when(giftCertificateRepository.update(giftCertificate)).thenReturn(giftCertificate);
            when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

            GiftCertificateDtoResponse response = giftCertificateService.updateGiftCertificateByIdPartially(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateRepository).update(giftCertificateCaptor.capture());
            verify(giftCertificateMapper).toDto(any());

            assertAll(
                    () -> assertThat(response).isEqualTo(giftCertificateDtoResponse),
                    () -> assertThat(giftCertificateCaptor.getValue()).isEqualTo(giftCertificate)
            );
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkPartialUpdateGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();

            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.updateGiftCertificateByIdPartially(TEST_ID, giftCertificateDtoRequest));

            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteGiftCertificateByIdTest {
        @DisplayName("Delete Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {4L, 5L, 6L})
        void checkDeleteGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            doNothing().when(giftCertificateRepository).deleteById(id);

            giftCertificateService.deleteGiftCertificateById(id);

            verify(giftCertificateRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Delete Gift Certificate by ID; not found")
        void checkDeleteGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).deleteById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.deleteGiftCertificateById(TEST_ID));

            verify(giftCertificateRepository).deleteById(anyLong());
        }
    }
}
