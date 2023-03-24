package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.list.GiftCertificateListMapper;
import ru.clevertec.ecl.mapper.list.TagListMapper;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.model.dto.response.GiftCertificateDtoResponse;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateMapper giftCertificateMapper;
    private final GiftCertificateListMapper giftCertificateListMapper;
    private final TagListMapper tagListMapper;

    @Override
    public GiftCertificateDtoResponse createGiftCertificate(GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.toEntity(giftCertificateDtoRequest);
        giftCertificate.setCreateDate(OffsetDateTime.now());
        giftCertificate.setLastUpdateDate(OffsetDateTime.now());
        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);

        return giftCertificateMapper.toDtoResponse(savedGiftCertificate);
    }

    @Override
    public List<GiftCertificateDtoResponse> getAllGiftCertificates() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll();
        return giftCertificateListMapper.toDtoResponse(giftCertificates);
    }

    @Override
    public GiftCertificateDtoResponse getGiftCertificateById(Long id) {
        return giftCertificateRepository.findById(id)
                .map(giftCertificateMapper::toDtoResponse)
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
        return giftCertificateMapper.toDtoResponse(savedGiftCertificate);
    }

    @Override
    public GiftCertificateDtoResponse updateGiftCertificateByIdPartially(Long id, GiftCertificateDtoRequest giftCertificateDtoRequest) {
        GiftCertificate updatedGiftCertificate = giftCertificateRepository.findById(id)
                .map(giftCertificate -> {
                    Optional.ofNullable(giftCertificateDtoRequest.getName()).ifPresent(giftCertificate::setName);
                    Optional.ofNullable(giftCertificateDtoRequest.getDescription()).ifPresent(giftCertificate::setDescription);
                    Optional.ofNullable(giftCertificateDtoRequest.getPrice()).ifPresent(giftCertificate::setPrice);
                    Optional.ofNullable(giftCertificateDtoRequest.getDuration()).ifPresent(giftCertificate::setDuration);
                    Optional.ofNullable(giftCertificateDtoRequest.getTags())
                            .ifPresent(tagDtoList -> giftCertificate.setTags(tagListMapper.toEntity(tagDtoList)));

                    giftCertificate.setLastUpdateDate(OffsetDateTime.now());

                    return giftCertificate;
                })
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));

        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(updatedGiftCertificate);
        return giftCertificateMapper.toDtoResponse(savedGiftCertificate);
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
