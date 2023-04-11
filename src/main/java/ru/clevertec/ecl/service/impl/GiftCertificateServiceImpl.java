package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.list.TagListMapper;
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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateSearcher giftCertificateSearcher;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateMapper giftCertificateMapper;
    private final TagListMapper tagListMapper;

    @Override
    public GiftCertificateDtoResponse createGiftCertificate(GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.toEntity(giftCertificateDtoRequest);
        giftCertificate.setCreateDate(OffsetDateTime.now());
        giftCertificate.setLastUpdateDate(OffsetDateTime.now());

        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);
        return giftCertificateMapper.toDto(savedGiftCertificate);
    }

    @Override
    public PageResponse<GiftCertificateDtoResponse> getAllGiftCertificates(Integer page, Integer pageSize) {
        Page<GiftCertificate> giftCertificatePage = giftCertificateRepository.findAll(PageRequest.of(page, pageSize));

        List<GiftCertificateDtoResponse> giftCertificateDtoResponses = giftCertificatePage.stream()
                .map(giftCertificateMapper::toDto)
                .toList();

        return PageResponse.<GiftCertificateDtoResponse>builder()
                .content(giftCertificateDtoResponses)
                .number(page)
                .size(pageSize)
                .numberOfElements(giftCertificateDtoResponses.size())
                .build();
    }

    @Override
    public PageResponse<GiftCertificateDtoResponse> getAllGiftCertificatesByCriteria(GiftCertificateCriteria searchCriteria) {
        Page<GiftCertificate> giftCertificatePage = giftCertificateSearcher.getGiftCertificatesByCriteria(searchCriteria);

        List<GiftCertificateDtoResponse> giftCertificateDtoResponses = giftCertificatePage.stream()
                .map(giftCertificateMapper::toDto)
                .toList();

        return PageResponse.<GiftCertificateDtoResponse>builder()
                .content(giftCertificateDtoResponses)
                .number(searchCriteria.getLimit())
                .size(searchCriteria.getOffset())
                .numberOfElements(giftCertificateDtoResponses.size())
                .build();
    }

    @Override
    public GiftCertificateDtoResponse getGiftCertificateById(Long id) {
        return giftCertificateRepository.findById(id)
                .map(giftCertificateMapper::toDto)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
    }

    @Override
    public GiftCertificateDtoResponse updateGiftCertificateById(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.toEntity(giftCertificateDtoRequest);
        giftCertificate.setId(id);
        giftCertificate.setLastUpdateDate(OffsetDateTime.now());

        if (giftCertificate.getCreateDate() == null) {
            giftCertificate.setCreateDate(OffsetDateTime.now());
        }

        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);
        return giftCertificateMapper.toDto(savedGiftCertificate);
    }

    @Override
    public GiftCertificateDtoResponse updateGiftCertificateByIdPartially(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate updatedGiftCertificate = giftCertificateRepository.findById(id)
                .map(giftCertificate -> {
                    Optional.ofNullable(giftCertificateDtoRequest.getName()).ifPresent(giftCertificate::setName);
                    Optional.ofNullable(giftCertificateDtoRequest.getDescription()).ifPresent(giftCertificate::setDescription);
                    Optional.ofNullable(giftCertificateDtoRequest.getPrice()).ifPresent(giftCertificate::setPrice);
                    Optional.ofNullable(giftCertificateDtoRequest.getDuration()).ifPresent(duration -> giftCertificate.setDuration(Duration.ofDays(duration)));
                    giftCertificate.setLastUpdateDate(OffsetDateTime.now());

                    Optional.ofNullable(giftCertificateDtoRequest.getTags())
                            .ifPresent(tagDtoList -> {
                                giftCertificate.getTags().clear();
                                giftCertificate.setTags(tagListMapper.toEntity(tagDtoList));
                            });

                    return giftCertificate;
                })
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));

        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(updatedGiftCertificate);
        return giftCertificateMapper.toDto(savedGiftCertificate);
    }

    @Override
    public GiftCertificateDtoResponse addTagToGiftCertificate(Long giftCertificateId, Long tagId) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateId)
                .orElseThrow(() -> new GiftCertificateNotFoundException(giftCertificateId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        giftCertificate.getTags().add(tag);

        return giftCertificateMapper.toDto(giftCertificateRepository.save(giftCertificate));
    }

    @Override
    public GiftCertificateDtoResponse deleteTagFromGiftCertificate(Long giftCertificateId, Long tagId) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateId)
                .orElseThrow(() -> new GiftCertificateNotFoundException(giftCertificateId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        giftCertificate.getTags().remove(tag);

        return giftCertificateMapper.toDto(giftCertificateRepository.save(giftCertificate));
    }

    @Override
    public void deleteGiftCertificateById(Long id) {
        try {
            giftCertificateRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new GiftCertificateNotFoundException(id);
        }
    }
}
