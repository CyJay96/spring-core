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
    public List<Tag> findAll() {
        String SQL = "select * from tags";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> findById(Long id) {
        String SQL = "select * from tags where id = ?";
        return jdbcTemplate.query(SQL, new Object[] {id}, new BeanPropertyRowMapper<>(Tag.class))
                .stream()
                .findAny();
    }

    @Override
    public void deleteById(Long id) {
        String SQL = "delete from tags where id = ?";
        jdbcTemplate.update(SQL, id);
    }
}
