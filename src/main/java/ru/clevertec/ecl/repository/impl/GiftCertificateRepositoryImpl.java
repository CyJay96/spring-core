package ru.clevertec.ecl.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(giftCertificate);
            session.getTransaction().commit();
            return giftCertificate;
        }
    }

    @Override
    public List<GiftCertificate> findAll(Integer page, Integer pageSize) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String SQL = "select gc from GiftCertificate gc";
            Query<GiftCertificate> query = session.createQuery(SQL, GiftCertificate.class);
            query.setFirstResult(page * pageSize);
            query.setMaxResults((page + 1) * pageSize);
            return query.list();
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
            session.delete(giftCertificate);
            session.persist(giftCertificate);
            session.getTransaction().commit();
            return giftCertificate;
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
