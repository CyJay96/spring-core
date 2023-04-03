package ru.clevertec.ecl.repository.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.util.HibernateUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Serializable id = session.save(giftCertificate);
            session.getTransaction().commit();
            return session.find(GiftCertificate.class, id);
        }
    }

    @Override
    public List<GiftCertificate> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "select gc from GiftCertificate gc";
            return session.createQuery(query, GiftCertificate.class).list();
        }
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            GiftCertificate giftCertificate = session.find(GiftCertificate.class, id);
            return Optional.of(giftCertificate);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(giftCertificate);
            session.getTransaction().commit();
            return session.find(GiftCertificate.class, giftCertificate.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            GiftCertificate giftCertificate = session.find(GiftCertificate.class, id);
            session.delete(giftCertificate);
            session.getTransaction().commit();
        }
    }
}
