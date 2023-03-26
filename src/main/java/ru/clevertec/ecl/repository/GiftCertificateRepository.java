package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    GiftCertificate save(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll();

    Optional<GiftCertificate> findById(Long id);

    List<GiftCertificate> findAllById(Iterable<Long> ids);

    void deleteById(Long id);
}
