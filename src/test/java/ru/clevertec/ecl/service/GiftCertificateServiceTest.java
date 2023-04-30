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
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.tag.TagTestBuilder;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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

    @Captor
    ArgumentCaptor<GiftCertificate> giftCertificateCaptor;

    private final GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder
            .aGiftCertificateDtoRequest()
            .build();
    private final GiftCertificateDtoResponse expectedGiftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder
            .aGiftCertificateDtoResponse()
            .build();
    private final GiftCertificate expectedGiftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
    private final GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder().build();
    private final TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();
    private final Tag tag = TagTestBuilder.aTag().build();
    private final Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateSearcher, giftCertificateRepository,
                tagRepository, giftCertificateMapper);
    }

    @Nested
    public class SaveTest {
        @Test
        @DisplayName("Save Gift Certificate")
        void checkSaveShouldReturnGiftCertificateDtoResponse() {
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(expectedGiftCertificate);
            doReturn(expectedGiftCertificate).when(giftCertificateMapper).toGiftCertificate(giftCertificateDtoRequest);
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.save(giftCertificateDtoRequest);

            verify(giftCertificateRepository).save(any());
            verify(giftCertificateMapper).toGiftCertificate(any());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Save Gift Certificate with Argument Captor")
        void checkSaveWithArgumentCaptorShouldReturnGiftCertificateDtoResponse() {
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(expectedGiftCertificate);
            doReturn(expectedGiftCertificate).when(giftCertificateMapper).toGiftCertificate(giftCertificateDtoRequest);
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

            giftCertificateService.save(giftCertificateDtoRequest);

            verify(giftCertificateRepository).save(giftCertificateCaptor.capture());
            verify(giftCertificateMapper).toGiftCertificate(any());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(giftCertificateCaptor.getValue()).isEqualTo(expectedGiftCertificate);
        }
    }

    @Test
    @DisplayName("Find all Gift Certificates")
    void checkFindAllShouldReturnGiftCertificateDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedGiftCertificate)))
                .when(giftCertificateRepository).findAll(pageable);
        doReturn(expectedGiftCertificateDtoResponse)
                .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

        PageResponse<GiftCertificateDtoResponse> actualGiftCertificates = giftCertificateService.findAll(pageable);

        verify(giftCertificateRepository).findAll(eq(pageable));
        verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

        assertThat(actualGiftCertificates.getContent().stream()
                .anyMatch(actualGiftCertificateDtoResponse ->
                        actualGiftCertificateDtoResponse.equals(expectedGiftCertificateDtoResponse))
        ).isTrue();
    }

    @Test
    @DisplayName("Find all Gift Certificates by criteria")
    void checkFindAllByCriteriaShouldReturnGiftCertificateDtoResponsePage() {
        doReturn(new PageImpl<>(List.of(expectedGiftCertificate))).
                when(giftCertificateSearcher).getGiftCertificatesByCriteria(searchCriteria);
        doReturn(expectedGiftCertificateDtoResponse).
                when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

        PageResponse<GiftCertificateDtoResponse> actualGiftCertificates = giftCertificateService
                .findAllByCriteria(searchCriteria, pageable);

        verify(giftCertificateSearcher).getGiftCertificatesByCriteria(any());
        verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

        assertThat(actualGiftCertificates.getContent().stream()
                .anyMatch(actualGiftCertificateDtoResponse ->
                        actualGiftCertificateDtoResponse.equals(expectedGiftCertificateDtoResponse))
        ).isTrue();
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(expectedGiftCertificateDtoResponse).when(giftCertificateMapper)
                    .toGiftCertificateDtoResponse(expectedGiftCertificate);

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.findById(id);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Find Gift Certificate by ID; not found")
        void checkFindByIdShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> giftCertificateService.findById(TEST_ID));

            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Nested
    public class UpdateTest {
        @DisplayName("Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(expectedGiftCertificate);
            doReturn(expectedGiftCertificate).when(giftCertificateMapper).toGiftCertificate(giftCertificateDtoRequest);
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService.update(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateRepository).save(any());
            verify(giftCertificateMapper).toGiftCertificate(any());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificateDtoResponse);
        }

        @DisplayName("Update Gift Certificate by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateWithArgumentCaptorShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(expectedGiftCertificate);
            doReturn(expectedGiftCertificate).when(giftCertificateMapper).toGiftCertificate(giftCertificateDtoRequest);
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

            giftCertificateService.update(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateRepository).save(giftCertificateCaptor.capture());
            verify(giftCertificateMapper).toGiftCertificate(any());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(giftCertificateCaptor.getValue()).isEqualTo(expectedGiftCertificate);
        }

        @DisplayName("Partial Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder
                    .aGiftCertificateDtoRequest()
                    .withTags(List.of(tagDtoRequest))
                    .build();

            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(expectedGiftCertificate);
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);
            doNothing().when(giftCertificateMapper).updateGiftCertificate(giftCertificateDtoRequest, expectedGiftCertificate);

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .updatePartially(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateRepository).save(any());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());
            verify(giftCertificateMapper).updateGiftCertificate(any(), any());

            assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificateDtoResponse);
        }

        @DisplayName("Partial Update Gift Certificate by ID with Argument Captor")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyWithArgumentCaptorShouldReturnGiftCertificateDtoResponse(Long id) {
            GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder
                    .aGiftCertificateDtoRequest()
                    .withTags(List.of(tagDtoRequest))
                    .build();

            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(expectedGiftCertificate);
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);
            doNothing().when(giftCertificateMapper).updateGiftCertificate(giftCertificateDtoRequest, expectedGiftCertificate);

            giftCertificateService.updatePartially(id, giftCertificateDtoRequest);

            verify(giftCertificateRepository).findById(anyLong());
            verify(giftCertificateRepository).save(giftCertificateCaptor.capture());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());
            verify(giftCertificateMapper).updateGiftCertificate(any(), any());

            assertThat(giftCertificateCaptor.getValue()).isEqualTo(expectedGiftCertificate);
        }

        @Test
        @DisplayName("Update Gift Certificate by ID; not found")
        void checkUpdateShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.update(TEST_ID, giftCertificateDtoRequest)
            );

            verify(giftCertificateRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkUpdatePartiallyShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.updatePartially(TEST_ID, giftCertificateDtoRequest)
            );

            verify(giftCertificateRepository).findById(anyLong());
        }
    }

    @Nested
    public class AddTagToGiftCertificateTest {
        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag already added")
        void checkAddTagToGiftCertificateWhenTagAlreadyAddedShouldReturnGiftCertificateDtoResponse() {
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate()
                    .withTags(List.of(tag))
                    .build();

            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(TEST_ID);
            doReturn(Optional.of(tag)).when(tagRepository).findById(TEST_ID);
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(giftCertificate);

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .addTagToGiftCertificate(TEST_ID, tag.getId());

            verify(giftCertificateRepository).findById(anyLong());
            verify(tagRepository).findById(anyLong());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag not already added")
        void checkAddTagToGiftCertificateWhenTagNotAlreadyAddedShouldReturnGiftCertificateDtoResponse() {
            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(TEST_ID);
            doReturn(Optional.of(tag)).when(tagRepository).findById(TEST_ID);
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(any());
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .addTagToGiftCertificate(TEST_ID, TEST_ID);

            verify(giftCertificateRepository).findById(anyLong());
            verify(tagRepository).findById(anyLong());
            verify(giftCertificateRepository).save(any());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Gift Certificate not found")
        void checkAddTagToGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.addTagToGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(giftCertificateRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag not found")
        void checkAddTagToGiftCertificateShouldThrowTagNotFoundException() {
            doReturn(Optional.of(expectedGiftCertificate))
                    .when(giftCertificateRepository).findById(anyLong());
            doThrow(EntityNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.addTagToGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(tagRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteTagFromGiftCertificateTest {
        @Test
        @DisplayName("Delete Tag from Gift Certificate")
        void checkDeleteTagFromGiftCertificateShouldReturnGiftCertificateDtoResponse() {
            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(TEST_ID);
            doReturn(Optional.of(tag)).when(tagRepository).findById(TEST_ID);
            doReturn(expectedGiftCertificate).when(giftCertificateRepository).save(any());
            doReturn(expectedGiftCertificateDtoResponse)
                    .when(giftCertificateMapper).toGiftCertificateDtoResponse(expectedGiftCertificate);

            GiftCertificateDtoResponse actualGiftCertificate = giftCertificateService
                    .deleteTagFromGiftCertificate(TEST_ID, TEST_ID);

            verify(giftCertificateRepository).findById(anyLong());
            verify(tagRepository).findById(anyLong());
            verify(giftCertificateRepository).save(any());
            verify(giftCertificateMapper).toGiftCertificateDtoResponse(any());

            assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificateDtoResponse);
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Gift Certificate not found")
        void checkDeleteTagFromGiftCertificateShouldThrowGiftCertificateNotFoundException() {
            doThrow(EntityNotFoundException.class).when(giftCertificateRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.deleteTagFromGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(giftCertificateRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Tag not found")
        void checkDeleteTagFromGiftCertificateShouldThrowTagNotFoundException() {
            doReturn(Optional.of(expectedGiftCertificate)).when(giftCertificateRepository).findById(anyLong());
            doThrow(EntityNotFoundException.class).when(tagRepository).findById(anyLong());

            assertThrows(EntityNotFoundException.class,
                    () -> giftCertificateService.deleteTagFromGiftCertificate(TEST_ID, TEST_ID)
            );

            verify(tagRepository).findById(anyLong());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnGiftCertificateDtoResponse(Long id) {
            doReturn(true).when(giftCertificateRepository).existsById(id);
            doNothing().when(giftCertificateRepository).deleteById(id);

            giftCertificateService.deleteById(id);

            verify(giftCertificateRepository).existsById(anyLong());
            verify(giftCertificateRepository).deleteById(anyLong());
        }

        @Test
        @DisplayName("Delete Gift Certificate by ID; not found")
        void checkDeleteByIdShouldThrowGiftCertificateNotFoundException() {
            doReturn(false).when(giftCertificateRepository).existsById(anyLong());

            assertThrows(EntityNotFoundException.class, () -> giftCertificateService.deleteById(TEST_ID));

            verify(giftCertificateRepository).existsById(anyLong());
        }
    }
}
