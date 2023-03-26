package ru.clevertec.ecl.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.mapper.jdbc.GiftCertificateJdbcMapper;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TagRepository tagRepository;

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        if (giftCertificate.getId() != null) {
            return update(giftCertificate);
        }

        String insertSQL = "insert into gift_certificates(name, description, price, duration, create_date, last_update_date) values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSQL,
                giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(),
                giftCertificate.getDuration().toNanos(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate());

        String selectSQL = "select last_value from gift_certificates_id_seq";
        Long id = jdbcTemplate.queryForObject(selectSQL, Long.class);

        giftCertificate.setId(id);
        saveTagsByGiftCertificate(giftCertificate);

        return findById(id).get();
    }

    private GiftCertificate update(GiftCertificate giftCertificate) {
        String updateSQL = "update gift_certificates set name = ?, description = ?, price = ?, duration = ?, last_update_date = ? where id = ?";
        jdbcTemplate.update(updateSQL,
                giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(),
                giftCertificate.getDuration().toNanos(), giftCertificate.getLastUpdateDate(), giftCertificate.getId());

        saveTagsByGiftCertificate(giftCertificate);

        return findById(giftCertificate.getId()).get();
    }

    private void saveTagsByGiftCertificate(GiftCertificate giftCertificate) {
        giftCertificate.getTags().forEach(tag -> {
            Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());

            Long tagId;

            if (tagOptional.isPresent()) {
                tagId = tagOptional.get().getId();
            } else {
                Tag savedTag = tagRepository.save(tag);
                tagId = savedTag.getId();
            }

            String relationSQL = "insert into gift_certificates_tags(gift_certificate_id, tag_id) values (?, ?)";
            jdbcTemplate.update(relationSQL, giftCertificate.getId(), tagId);
        });
    }

    @Override
    public List<GiftCertificate> findAll() {
        String SQL = "select * from gift_certificates";
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(SQL, new GiftCertificateJdbcMapper());

        giftCertificates.forEach(giftCertificate -> {
            String relationSQL = "select tag_id from gift_certificates_tags where gift_certificate_id = ?";
            List<Long> tagsIds = jdbcTemplate.queryForList(relationSQL, Long.class, giftCertificate.getId());
            List<Tag> tags = tagRepository.findAllById(tagsIds);
            giftCertificate.setTags(tags);
        });

        return giftCertificates;
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        String SQL = "select * from gift_certificates where id = ?";
        Optional<GiftCertificate> giftCertificate = jdbcTemplate.query(SQL, new Object[]{id}, new GiftCertificateJdbcMapper())
                .stream()
                .findAny();

        if (giftCertificate.isPresent()) {
            String relationSQL = "select tag_id from gift_certificates_tags where gift_certificate_id = ?";
            List<Long> tagsIds = jdbcTemplate.queryForList(relationSQL, Long.class, id);
            List<Tag> tags = tagRepository.findAllById(tagsIds);
            giftCertificate.get().setTags(tags);
        }

        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> findAllById(Iterable<Long> ids) {
        List<GiftCertificate> giftCertificates = new ArrayList<>();

        String SQL = "select * from gift_certificates where id = ?";
        ids.forEach(id -> giftCertificates.addAll(jdbcTemplate.query(SQL, new Object[] {id}, new GiftCertificateJdbcMapper())));

        return giftCertificates;
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteAllByGiftCertificateId(id);

        String SQL = "delete from gift_certificates where id = ?";
        jdbcTemplate.update(SQL, id);
    }
}
