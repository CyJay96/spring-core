package ru.clevertec.ecl.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Tag save(Tag tag) {
        if (tag.getId() == null) {
            jdbcTemplate.update("insert into tags(name) values(?)", tag.getName());
            List<Tag> tags = jdbcTemplate.query("select * from tags", new BeanPropertyRowMapper<>(Tag.class));
            return tags.stream()
                    .skip(tags.size() - 1)
                    .findFirst()
                    .get();
        }
        jdbcTemplate.update("update tags set name = ? where id = ?", tag.getName(), tag.getId());
        return jdbcTemplate.query("select * from tags where id = ?", new Object[] {tag.getId()}, new BeanPropertyRowMapper<>(Tag.class))
                .stream()
                .findAny()
                .get();
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query("select * from tags", new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return jdbcTemplate.query("select * from tags where id = ?", new Object[] {id}, new BeanPropertyRowMapper<>(Tag.class))
                .stream()
                .findAny();
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from tags where id = ?", id);
    }
}
