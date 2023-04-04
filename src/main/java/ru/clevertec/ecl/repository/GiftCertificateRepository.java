package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {

    GiftCertificate save(GiftCertificate giftCertificate);

    GiftCertificate update(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll(Integer page, Integer pageSize);

    Optional<GiftCertificate> findById(Long id);

    void deleteById(Long id);
}
