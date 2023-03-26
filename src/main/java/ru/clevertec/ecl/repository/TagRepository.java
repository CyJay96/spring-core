package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    List<Tag> findAll();

    List<Tag> findAllById(Iterable<Long> ids);

    List<Tag> findAllByGiftCertificateId(Long id);

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    void deleteById(Long id);

    void deleteAllByGiftCertificateId(Long id);
}
