package ru.clevertec.ecl.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.mapper.jdbc.GiftCertificateJdbcMapper;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        if (giftCertificate.getId() == null) {
            String insertSQL = "insert into gift_certificates(name, description, price, duration, create_date, last_update_date) values(?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertSQL,
                    giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(),
                    giftCertificate.getDuration().toNanos(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate());

            String selectSQL = "select last_value from gift_certificates_id_seq";
            Long id = jdbcTemplate.queryForObject(selectSQL, Long.class);

            return findById(id).get();
        }

        String updateSQL = "update gift_certificates set name = ?, description = ?, price = ?, duration = ?, last_update_date = ? where id = ?";
        jdbcTemplate.update(updateSQL,
                giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(),
                giftCertificate.getDuration().toNanos(), giftCertificate.getLastUpdateDate(), giftCertificate.getId());

        return findById(giftCertificate.getId()).get();
    }

    @Override
    public List<GiftCertificate> findAll() {
        String SQL = "select * from gift_certificates";
        return jdbcTemplate.query(SQL, new GiftCertificateJdbcMapper());
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        String SQL = "select * from gift_certificates where id = ?";
        return jdbcTemplate.query(SQL, new Object[] {id}, new GiftCertificateJdbcMapper())
                .stream()
                .findAny();
    }

    @Override
    public void deleteById(Long id) {
        String SQL = "delete from gift_certificates where id = ?";
        jdbcTemplate.update(SQL, id);
    }
}
