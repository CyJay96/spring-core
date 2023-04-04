package ru.clevertec.ecl.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.util.HibernateUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository {

    @Override
    public Tag save(Tag tag) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Serializable id = session.save(tag);
            session.getTransaction().commit();
            return session.find(Tag.class, id);
        }
    }

    @Override
    public List<Tag> findAll(Integer page, Integer pageSize) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String SQL = "select t from Tag t";
            Query<Tag> query = session.createQuery(SQL, Tag.class);
            query.setFirstResult(page * pageSize);
            query.setMaxResults((page + 1) * pageSize);
            return query.list();
        }
    }

    @Override
    public Optional<Tag> findById(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Tag tag = session.find(Tag.class, id);
            return Optional.of(tag);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tag update(Tag tag) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(tag);
            session.getTransaction().commit();
            return session.find(Tag.class, tag.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Tag tag = session.get(Tag.class, id);
            session.delete(tag);
            session.getTransaction().commit();
        }
    }
}
