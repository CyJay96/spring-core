package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;

public interface GiftCertificateService {

    GiftCertificateDtoResponse createGiftCertificate(GiftCertificateDtoRequest giftCertificateDtoRequest);

    PageResponse<GiftCertificateDtoResponse> getAllGiftCertificates(Integer page, Integer pageSize);

    PageResponse<GiftCertificateDtoResponse> getAllGiftCertificatesByCriteria(GiftCertificateCriteria searchCriteria);

    GiftCertificateDtoResponse getGiftCertificateById(Long id);

    GiftCertificateDtoResponse updateGiftCertificateById(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest);

    GiftCertificateDtoResponse updateGiftCertificateByIdPartially(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest);

    GiftCertificateDtoResponse addTagToGiftCertificate(Long giftCertificateId, Long tagId);

    void deleteGiftCertificateById(Long id);
}
