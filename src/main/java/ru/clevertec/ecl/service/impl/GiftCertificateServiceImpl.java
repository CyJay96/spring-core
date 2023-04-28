package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.searcher.GiftCertificateSearcher;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateSearcher giftCertificateSearcher;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateMapper giftCertificateMapper;

    @Override
    public GiftCertificateDtoResponse save(GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.toGiftCertificate(giftCertificateDtoRequest);
        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);
        return giftCertificateMapper.toGiftCertificateDtoResponse(savedGiftCertificate);
    }

    @Override
    public PageResponse<GiftCertificateDtoResponse> findAll(Pageable pageable) {
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAll(pageable);

        List<GiftCertificateDtoResponse> giftCertificateDtoResponses = giftCertificatePage.stream()
                .map(giftCertificateMapper::toGiftCertificateDtoResponse)
                .toList();

        return PageResponse.<GiftCertificateDtoResponse>builder()
                .content(giftCertificateDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(giftCertificateDtoResponses.size())
                .build();
    }

    @Override
    public PageResponse<GiftCertificateDtoResponse> findAllByCriteria(
            GiftCertificateCriteria searchCriteria,
            Pageable pageable) {
        searchCriteria = Objects.requireNonNullElse(searchCriteria, GiftCertificateCriteria.builder().build());

        searchCriteria.setOffset(pageable.getPageNumber());
        searchCriteria.setLimit(pageable.getPageSize());

        Page<GiftCertificate> giftCertificatePage = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria);

        List<GiftCertificateDtoResponse> giftCertificateDtoResponses = giftCertificatePage.stream()
                .map(giftCertificateMapper::toGiftCertificateDtoResponse)
                .toList();

        return PageResponse.<GiftCertificateDtoResponse>builder()
                .content(giftCertificateDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(giftCertificateDtoResponses.size())
                .build();
    }

    @Override
    public GiftCertificateDtoResponse findById(Long id) {
        return giftCertificateRepository.findById(id)
                .map(giftCertificateMapper::toGiftCertificateDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificate.class, id));
    }

    @Override
    public GiftCertificateDtoResponse update(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificate.class, id));

        GiftCertificate updatedGiftCertificate = giftCertificateMapper.toGiftCertificate(giftCertificateDtoRequest);
        updatedGiftCertificate.setId(id);
        updatedGiftCertificate.setCreateDate(giftCertificate.getCreateDate());
        updatedGiftCertificate.setLastUpdateDate(OffsetDateTime.now());

        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(updatedGiftCertificate);
        return giftCertificateMapper.toGiftCertificateDtoResponse(savedGiftCertificate);
    }

    @Override
    public GiftCertificateDtoResponse updatePartially(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate updatedGiftCertificate = giftCertificateRepository.findById(id)
                .map(giftCertificate -> {
                    giftCertificateMapper.updateGiftCertificate(giftCertificateDtoRequest, giftCertificate);
                    return giftCertificate;
                })
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificate.class, id));

        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(updatedGiftCertificate);
        return giftCertificateMapper.toGiftCertificateDtoResponse(savedGiftCertificate);
    }

    @Override
    public GiftCertificateDtoResponse addTagToGiftCertificate(Long giftCertificateId, Long tagId) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateId)
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificate.class, giftCertificateId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, tagId));

        boolean alreadyAdded = giftCertificate.getTags().stream()
                .map(Tag::getId)
                .anyMatch(id -> id.equals(tagId));

        if (alreadyAdded) {
            return giftCertificateMapper.toGiftCertificateDtoResponse(giftCertificate);
        }

        giftCertificate.getTags().add(tag);

        return giftCertificateMapper.toGiftCertificateDtoResponse(giftCertificateRepository.save(giftCertificate));
    }

    @Override
    public GiftCertificateDtoResponse deleteTagFromGiftCertificate(Long giftCertificateId, Long tagId) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateId)
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificate.class, giftCertificateId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, tagId));

        giftCertificate.getTags().remove(tag);

        return giftCertificateMapper.toGiftCertificateDtoResponse(giftCertificateRepository.save(giftCertificate));
    }

    @Override
    public void deleteById(Long id) {
        if (!giftCertificateRepository.existsById(id)) {
            throw new EntityNotFoundException(GiftCertificate.class, id);
        }
        giftCertificateRepository.deleteById(id);
    }
}
