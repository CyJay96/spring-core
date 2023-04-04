package ru.clevertec.ecl.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public Tag save(Tag tag) {
        if (tag.getId() == null) {
            Optional<Tag> tagOptional = findByName(tag.getName());
            if (tagOptional.isPresent()) {
                return findByName(tagOptional.get().getName()).get();
            }

            String updateSQL = "insert into tags(name) values(?)";
            jdbcTemplate.update(updateSQL, tag.getName());

            String selectSQL = "select last_value from tags_id_seq";
            Long id = jdbcTemplate.queryForObject(selectSQL, Long.class);

            return findById(id).get();
        }

        String updateSQL = "update tags set name = ? where id = ?";
        jdbcTemplate.update(updateSQL, tag.getName(), tag.getId());

        return findById(tag.getId()).get();
    }

    @Override
    @Transactional
    public List<Tag> findAll() {
        String SQL = "select * from tags";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    @Transactional
    public List<Tag> findAllById(Iterable<Long> ids) {
        List<Tag> tags = new ArrayList<>();

        String SQL = "select * from tags where id = ?";
        ids.forEach(id -> tags.addAll(jdbcTemplate.query(SQL, new Object[] {id}, new BeanPropertyRowMapper<>(Tag.class))));

        return tags;
    }

    @Override
    @Transactional
    public List<Tag> findAllByGiftCertificateId(Long id) {
        String relationSQL = "select tag_id from gift_certificates_tags where gift_certificate_id = ?";
        List<Long> tagsIds = jdbcTemplate.queryForList(relationSQL, Long.class, id);
        return findAllById(tagsIds);
    }

    @Override
    @Transactional
    public Optional<Tag> findById(Long id) {
        String SQL = "select * from tags where id = ?";
        return jdbcTemplate.query(SQL, new Object[] {id}, new BeanPropertyRowMapper<>(Tag.class))
                .stream()
                .findAny();
    }

    @Override
    @Transactional
    public Optional<Tag> findByName(String name) {
        String SQL = "select * from tags where lower(name) = lower(?)";
        return jdbcTemplate.query(SQL, new Object[] {name}, new BeanPropertyRowMapper<>(Tag.class))
                .stream()
                .findAny();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        String deleteSQL1 = "delete from gift_certificates_tags where tag_id = ?";
        jdbcTemplate.update(deleteSQL1, id);

        String deleteSQL2 = "delete from tags where id = ?";
        jdbcTemplate.update(deleteSQL2, id);
    }

    @Override
    @Transactional
    public void deleteAllByGiftCertificateId(Long id) {
        String relationSQL = "select tag_id from gift_certificates_tags where gift_certificate_id = ?";
        List<Long> tagsIds = jdbcTemplate.queryForList(relationSQL, Long.class, id);

        String deleteSQL1 = "delete from gift_certificates_tags where gift_certificate_id = ?";
        jdbcTemplate.update(deleteSQL1, id);

        tagsIds.forEach(tagId -> {
            String deleteSQL2 = "delete from gift_certificates_tags where tag_id = ?";
            jdbcTemplate.update(deleteSQL2, tagId);

            String deleteSQL3 = "delete from tags where id = ?";
            jdbcTemplate.update(deleteSQL3, tagId);
        });
    }
}
