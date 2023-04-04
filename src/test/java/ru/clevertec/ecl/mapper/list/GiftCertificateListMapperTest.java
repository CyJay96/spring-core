package ru.clevertec.ecl.mapper.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoResponseTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateListMapperTest {

    private GiftCertificateListMapper giftCertificateListMapper;

    @Mock
    private GiftCertificateMapper giftCertificateMapper;

    @Captor
    ArgumentCaptor<GiftCertificateDtoRequest> giftCertificateDtoRequestCaptor;

    @Captor
    ArgumentCaptor<GiftCertificate> giftCertificateCaptor;

    @BeforeEach
    void setUp() {
        giftCertificateListMapper = new GiftCertificateListMapperImpl(giftCertificateMapper);
    }

    @Test
    @DisplayName("Map Gift Certificate List DTO to Entity")
    void checkToEntityShouldReturnGiftCertificateList() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
        GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder.aGiftCertificateDtoRequest().build();

        when(giftCertificateMapper.toEntity(any())).thenReturn(giftCertificate);

        List<GiftCertificate> giftCertificateList = giftCertificateListMapper.toEntity(List.of(giftCertificateDtoRequest));

        verify(giftCertificateMapper).toEntity(giftCertificateDtoRequestCaptor.capture());

        assertAll(
                () -> assertThat(Objects.requireNonNull(giftCertificateList).size()).isEqualTo(1),
                () -> assertThat(Objects.requireNonNull(giftCertificateList).get(0)).isEqualTo(giftCertificate),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoRequestCaptor).getValue()).isEqualTo(giftCertificateDtoRequest)
        );
    }

    @Test
    @DisplayName("Map Gift Certificate List Entity to DTO")
    void checkToDtoShouldReturnGiftCertificateDtoResponseList() {
        GiftCertificate giftCertificate = GiftCertificateTestBuilder.aGiftCertificate().build();
        GiftCertificateDtoResponse giftCertificateDtoResponse = GiftCertificateDtoResponseTestBuilder.aGiftCertificateDtoResponse().build();

        when(giftCertificateMapper.toDto(any())).thenReturn(giftCertificateDtoResponse);

        List<GiftCertificateDtoResponse> giftCertificateDtoResponses = giftCertificateListMapper.toDto(List.of(giftCertificate));

        verify(giftCertificateMapper).toDto(giftCertificateCaptor.capture());

        assertAll(
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponses).size()).isEqualTo(1),
                () -> assertThat(Objects.requireNonNull(giftCertificateDtoResponses).get(0)).isEqualTo(giftCertificateDtoResponse),
                () -> assertThat(Objects.requireNonNull(giftCertificateCaptor).getValue()).isEqualTo(giftCertificate)
        );
    }
}
