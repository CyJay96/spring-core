package ru.clevertec.ecl.service.searcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.enums.SortType;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@ExtendWith(MockitoExtension.class)
class GiftCertificateSearcherTest {

    private GiftCertificateSearcher giftCertificateSearcher;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @BeforeEach
    void setUp() {
        giftCertificateSearcher = new GiftCertificateSearcher(giftCertificateRepository);
    }

    @Test
    @DisplayName("Get Gift Certificates by tag name")
    void checkGetGiftCertificatesByTagNameShouldReturnGiftCertificateList() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder()
                .tagName(TEST_STRING)
                .build();

        when(giftCertificateRepository.findAllByTagName(TEST_STRING)).thenReturn(List.of(giftCertificate));

        List<GiftCertificate> response = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria);

        verify(giftCertificateRepository).findAllByTagName(anyString());

        assertAll(
                () -> assertThat(response.size()).isEqualTo(1),
                () -> assertThat(response.get(0)).isEqualTo(giftCertificate)
        );
    }

    @Test
    @DisplayName("Get Gift Certificates by description")
    void checkGetGiftCertificatesByDescriptionShouldReturnGiftCertificateList() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder()
                .description(TEST_STRING)
                .build();

        when(giftCertificateRepository.findAllLikeDescription(TEST_STRING)).thenReturn(List.of(giftCertificate));

        List<GiftCertificate> response = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria);

        verify(giftCertificateRepository).findAllLikeDescription(anyString());

        assertAll(
                () -> assertThat(response.size()).isEqualTo(1),
                () -> assertThat(response.get(0)).isEqualTo(giftCertificate)
        );
    }

    @Test
    @DisplayName("Get Gift Certificates by sort type name and date")
    void checkGetGiftCertificatesBySortTypeNameAndSortTypeDateShouldReturnGiftCertificateList() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder()
                .sortTypeName(SortType.ASC)
                .sortTypeDate(SortType.DESC)
                .build();

        when(giftCertificateRepository.findAll()).thenReturn(List.of(giftCertificate));

        List<GiftCertificate> response = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria);

        verify(giftCertificateRepository).findAll();

        assertAll(
                () -> assertThat(response.size()).isEqualTo(1),
                () -> assertThat(response.get(0)).isEqualTo(giftCertificate)
        );
    }
}
