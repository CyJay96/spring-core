package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;

public interface GiftCertificateService {

    GiftCertificateDtoResponse save(GiftCertificateDtoRequest giftCertificateDtoRequest);

    PageResponse<GiftCertificateDtoResponse> findAll(Pageable pageable);

    PageResponse<GiftCertificateDtoResponse> findAllByCriteria(GiftCertificateCriteria searchCriteria, Pageable pageable);

    GiftCertificateDtoResponse findById(Long id);

    GiftCertificateDtoResponse update(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest);

    GiftCertificateDtoResponse updatePartially(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest);

    GiftCertificateDtoResponse addTagToGiftCertificate(Long giftCertificateId, Long tagId);

    GiftCertificateDtoResponse deleteTagFromGiftCertificate(Long giftCertificateId, Long tagId);

    void deleteById(Long id);
}
