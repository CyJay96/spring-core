package ru.clevertec.ecl.service.searcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.config.DBConfig;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.enums.SortType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

@ActiveProfiles("dev")
@ContextConfiguration(classes = DBConfig.class)
class GiftCertificateSearcherTest {

    private GiftCertificateSearcher giftCertificateSearcher;

    @BeforeEach
    void setUp() {
        giftCertificateSearcher = new GiftCertificateSearcher();
    }

//    @Test
//    @DisplayName("Get Gift Certificates by tag name")
//    void checkGetGiftCertificatesByTagNameShouldReturnGiftCertificateList() {
//        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
//
//        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder()
//                .tagName("Python")
//                .build();
//
//        List<GiftCertificate> response = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria, PAGE, PAGE_SIZE);
//
//        assertThat(response.size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Get Gift Certificates by description")
//    void checkGetGiftCertificatesByDescriptionShouldReturnGiftCertificateList() {
//        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
//
//        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder()
//                .description(TEST_STRING)
//                .build();
//
//        List<GiftCertificate> response = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria, PAGE, PAGE_SIZE);
//
//        assertThat(response.size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("Get Gift Certificates by sort type name and date")
//    void checkGetGiftCertificatesBySortTypeNameAndSortTypeDateShouldReturnGiftCertificateList() {
//        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
//
//        GiftCertificateCriteria searchCriteria = GiftCertificateCriteria.builder()
//                .sortTypeName(SortType.ASC)
//                .sortTypeDate(SortType.DESC)
//                .build();
//
//        List<GiftCertificate> response = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria, PAGE, PAGE_SIZE);
//
//        assertThat(response.size()).isEqualTo(1);
//    }
}
