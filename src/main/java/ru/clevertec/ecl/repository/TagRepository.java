package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    List<Tag> findAll();

    Optional<Tag> findById(Long id);

    void deleteById(Long id);
}
