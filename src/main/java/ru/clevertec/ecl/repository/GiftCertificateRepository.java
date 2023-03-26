package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    GiftCertificate save(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll();

    List<GiftCertificate> findAllByTagName(String tagName);

    List<GiftCertificate> findAllLikeDescription(String description);

    Optional<GiftCertificate> findById(Long id);

    void deleteById(Long id);
}
