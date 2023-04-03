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
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Serializable id = session.save(giftCertificate);
            session.getTransaction().commit();
            return session.find(GiftCertificate.class, id);
        }
    }

    @Override
    @Transactional
    public List<GiftCertificate> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "select gc from GiftCertificate gc";
            return session.createQuery(query, GiftCertificate.class).list();
        }
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> findById(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            GiftCertificate giftCertificate = session.find(GiftCertificate.class, id);
            return Optional.of(giftCertificate);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate giftCertificate) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(giftCertificate);
            session.getTransaction().commit();
            return session.find(GiftCertificate.class, giftCertificate.getId());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            GiftCertificate giftCertificate = session.find(GiftCertificate.class, id);
            session.delete(giftCertificate);
            session.getTransaction().commit();
        }
    }
}
