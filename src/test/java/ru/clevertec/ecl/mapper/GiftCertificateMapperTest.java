package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.clevertec.ecl.util.TestConstants.TEST_BIG_DECIMAL;
import static ru.clevertec.ecl.util.TestConstants.TEST_DATE;
import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
import static ru.clevertec.ecl.util.TestConstants.TEST_NUMBER;
import static ru.clevertec.ecl.util.TestConstants.TEST_STRING;

class GiftCertificateMapperTest {

    private GiftCertificateMapper giftCertificateMapper;

    private final GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();
    private final GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

    @BeforeEach
    void setUp() {
        giftCertificateMapper = new GiftCertificateMapperImpl();
    }

    @Test
    @DisplayName("Map Gift Certificate DTO to Entity")
    void checkToEntityShouldReturnGiftCertificate() {
        GiftCertificate giftCertificate = giftCertificateMapper.toEntity(giftCertificateDtoRequest);

        assertAll(
                () -> assertThat(Objects.requireNonNull(giftCertificate).getId()).isNull(),
                () -> assertThat(Objects.requireNonNull(giftCertificate).getName()).isEqualTo(TEST_STRING),
                () -> assertThat(Objects.requireNonNull(giftCertificate).getDescription()).isEqualTo(TEST_STRING),
                () -> assertThat(Objects.requireNonNull(giftCertificate).getPrice()).isEqualTo(TEST_BIG_DECIMAL),
                () -> assertThat(Objects.requireNonNull(giftCertificate).getDuration().toDays()).isEqualTo(TEST_NUMBER),
                () -> assertThat(Objects.requireNonNull(giftCertificate).getCreateDate()).isNull(),
                () -> assertThat(Objects.requireNonNull(giftCertificate).getLastUpdateDate()).isNull(),
                () -> assertThat(Objects.requireNonNull(giftCertificate).getTags()).isEmpty()
        );
    }

    @Test
    @DisplayName("Map Gift Certificate Entity to DTO")
    void checkToDtoShouldReturnGiftCertificateDtoResponse() {
        GiftCertificateDtoResponse giftCertificateDtoResponse = giftCertificateMapper.toDto(giftCertificate);

        assertAll(
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getId()).isEqualTo(TEST_ID),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getName()).isEqualTo(TEST_STRING),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getDescription()).isEqualTo(TEST_STRING),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getPrice()).isEqualTo(TEST_BIG_DECIMAL),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getDuration()).isEqualTo(TEST_NUMBER),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getCreateDate()).isEqualTo(TEST_DATE.toString()),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getLastUpdateDate()).isEqualTo(TEST_DATE.toString()),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponse).getTags()).isEmpty()
        );
    }
}
