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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.list.TagListMapper;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.impl.GiftCertificateServiceImpl;
import ru.clevertec.ecl.service.searcher.GiftCertificateSearcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    private GiftCertificateService giftCertificateService;

    @Mock
    private GiftCertificateSearcher giftCertificateSearcher;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private GiftCertificateMapper giftCertificateMapper;

    @Mock
    private TagListMapper tagListMapper;

    @Captor
    ArgumentCaptor<GiftCertificate> giftCertificateCaptor;

    private final GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
    private final GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();
    private final GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
    private final GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder().build();
    private final TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
    private final Tag tag = TagTestBuilder.aTag().build();

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateSearcher, giftCertificateRepository,
                tagRepository, giftCertificateMapper, tagListMapper);
    }

    @Test
    @DisplayName("Create Gift Certificate")
    void checkCreateGiftCertificateShouldReturnGiftCertificateDtoResponse() {
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
        when(giftCertificateRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE))).thenReturn(new PageImpl<>(List.of(giftCertificate)));
        when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

        PageResponse<GiftCertificateDtoResponse> response = giftCertificateService.getAllGiftCertificates(PAGE, PAGE_SIZE);

        verify(giftCertificateRepository).findAll(PageRequest.of(PAGE, PAGE_SIZE));
        verify(giftCertificateMapper).toDto(any());

        assertThat(response.getContent().stream()
                .anyMatch(giftCertificateDto -> giftCertificateDto.equals(giftCertificateDtoResponse))
        ).isTrue();
    }

    @Test
    @DisplayName("Get all Gift Certificates by criteria")
    void checkGetAllGiftCertificatesByCriteriaShouldReturnGiftCertificateDtoResponsePage() {
        when(giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria)).thenReturn(new PageImpl<>(List.of(giftCertificate)));
        when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

        PageResponse<GiftCertificateDtoResponse> response = giftCertificateService.getAllGiftCertificatesByCriteria(searchCriteria);

        verify(giftCertificateSearcher).getGiftCertificatesByCriteria(any());
        verify(giftCertificateMapper).toDto(any());

        assertThat(response.getContent().stream()
                .anyMatch(giftCertificateDto -> giftCertificateDto.equals(giftCertificateDtoResponse))
        ).isTrue();
    }

    @Nested
    public class GetProductByIdTest {
        @DisplayName("Get Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkGetGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) {
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
            when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
            when(giftCertificateMapper.toEntity(giftCertificateDtoRequest)).thenReturn(giftCertificate);
            when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

            GiftCertificateDtoResponse response = giftCertificateService.updateGiftCertificateById(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).save(giftCertificateCaptor.capture());
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
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest()
                    .withTags(List.of(tagDtoRequest))
                    .build();

            when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
            when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
            when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);
            when(tagListMapper.toEntity(List.of(tagDtoRequest))).thenReturn(List.of(tag));

            GiftCertificateDtoResponse response = giftCertificateService.updateGiftCertificateByIdPartially(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateRepository).save(giftCertificateCaptor.capture());
            verify(giftCertificateMapper).toDto(any());
            verify(tagListMapper).toEntity(any());

            assertAll(
                    () -> assertThat(response).isEqualTo(giftCertificateDtoResponse),
                    () -> assertThat(giftCertificateCaptor.getValue()).isEqualTo(giftCertificate)
            );
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkPartialUpdateGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.updateGiftCertificateByIdPartially(TEST_ID, giftCertificateDtoRequest));

            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Nested
    public class AddTagToGiftCertificateTest {
        @Test
        @DisplayName("Add Tag to Gift Certificate")
        void checkAddTagToGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate()
                    .withTags(new ArrayList<>())
                    .build();

            when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(giftCertificate));
            when(tagRepository.findById(TEST_ID)).thenReturn(Optional.of(tag));
            when(giftCertificateRepository.save(any())).thenReturn(giftCertificate);
            when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

            GiftCertificateDtoResponse response = giftCertificateService.addTagToGiftCertificate(TEST_ID, TEST_ID);

            verify(giftCertificateRepository).findById(anyLong());
            verify(tagRepository).findById(anyLong());
            verify(giftCertificateRepository).save(any());
            verify(giftCertificateMapper).toDto(any());

            assertThat(response).isEqualTo(giftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Gift Certificate not found")
        void checkAddTagToGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.addTagToGiftCertificate(TEST_ID, TEST_ID));

            verify(giftCertificateRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag not found")
        void checkAddTagToGiftCertificateShouldThrowTagNotFoundException() {
            when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(giftCertificate));
            doThrow(TagNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(TagNotFoundException.class, () -> giftCertificateService.addTagToGiftCertificate(TEST_ID, TEST_ID));

            verify(tagRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteTagFromGiftCertificateTest {
        @Test
        @DisplayName("Delete Tag from Gift Certificate")
        void checkDeleteTagFromGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(giftCertificate));
            when(tagRepository.findById(TEST_ID)).thenReturn(Optional.of(tag));
            when(giftCertificateRepository.save(any())).thenReturn(giftCertificate);
            when(giftCertificateMapper.toDto(giftCertificate)).thenReturn(giftCertificateDtoResponse);

            GiftCertificateDtoResponse response = giftCertificateService.deleteTagFromGiftCertificate(TEST_ID, TEST_ID);

            verify(giftCertificateRepository).findById(anyLong());
            verify(tagRepository).findById(anyLong());
            verify(giftCertificateRepository).save(any());
            verify(giftCertificateMapper).toDto(any());

            assertThat(response).isEqualTo(giftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Gift Certificate not found")
        void checkDeleteTagFromGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            doThrow(GiftCertificateNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(GiftCertificateNotFoundException.class, () -> giftCertificateService.deleteTagFromGiftCertificate(TEST_ID, TEST_ID));

            verify(giftCertificateRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Tag not found")
        void checkDeleteTagFromGiftCertificateShouldThrowTagNotFoundException() {
            when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(giftCertificate));
            doThrow(TagNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(TagNotFoundException.class, () -> giftCertificateService.deleteTagFromGiftCertificate(TEST_ID, TEST_ID));

            verify(tagRepository).findById(anyLong());
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
