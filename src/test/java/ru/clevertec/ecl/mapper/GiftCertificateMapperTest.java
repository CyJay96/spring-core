package ru.clevertec.ecl.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftCertificateMapperTest {

    private GiftCertificateMapper giftCertificateMapper;

    @Mock
    private TagMapper tagMapper;

    private final GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder
            .aGiftCertificateDtoRequest()
            .build();
    private final GiftCertificateDtoResponse expectedGiftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder
            .aGiftCertificateDtoResponse()
            .build();
    private final GiftCertificate expectedGiftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();

    @BeforeEach
    void setUp() {
        giftCertificateMapper = new GiftCertificateMapperImpl(tagMapper);
    }

    @Test
    @DisplayName("Map Gift Certificate DTO to Entity")
    void checkToGiftCertificateShouldReturnGiftCertificate() {
        doReturn(new ArrayList<>()).when(tagMapper).toTagList(any());

        GiftCertificate actualGiftCertificate = giftCertificateMapper.toGiftCertificate(giftCertificateDtoRequest);

        verify(tagMapper).toTagList(any());

        assertThat(actualGiftCertificate).isEqualTo(expectedGiftCertificate);
    }

    @Test
    @DisplayName("Map Gift Certificate Entity to DTO")
    void checkToGiftCertificateDtoResponseShouldReturnGiftCertificateDtoResponse() {
        GiftCertificateDtoResponse actualGiftCertificateDtoResponse = giftCertificateMapper
                .toGiftCertificateDtoResponse(expectedGiftCertificate);
        assertThat(actualGiftCertificateDtoResponse).isEqualTo(expectedGiftCertificateDtoResponse);
    }
}
