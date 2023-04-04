package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDtoResponse createGiftCertificate(GiftCertificateDtoRequest giftCertificateDtoRequest);

    List<GiftCertificateDtoResponse> getAllGiftCertificates();

    List<GiftCertificateDtoResponse> getAllGiftCertificatesByCriteria(GiftCertificateCriteria searchCriteria);

    GiftCertificateDtoResponse getGiftCertificateById(Long id);

    GiftCertificateDtoResponse updateGiftCertificateById(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest);

    GiftCertificateDtoResponse updateGiftCertificateByIdPartially(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest);

    void deleteGiftCertificateById(Long id);
}
