package ru.clevertec.ecl.mapper.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ru.clevertec.ecl.model.entity.GiftCertificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;

public class GiftCertificateJdbcMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong("id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getBigDecimal("price"));
        giftCertificate.setDuration(Duration.ofNanos(rs.getLong("duration")));
        giftCertificate.setCreateDate(rs.getObject("create_date", OffsetDateTime.class));
        giftCertificate.setLastUpdateDate(rs.getObject("last_update_date", OffsetDateTime.class));
        giftCertificate.setCreateDate(OffsetDateTime.now());
        giftCertificate.setLastUpdateDate(OffsetDateTime.now());
        return giftCertificate;
    }
}
