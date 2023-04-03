package ru.clevertec.ecl.repository.impl;

import org.hibernate.Session;
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
    public List<Tag> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "select t from Tag t";
            return session.createQuery(query, Tag.class).list();
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
    public Optional<Tag> findByName(String name) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "select t from Tag t where lower(t.name) = lower(:name)";
            List<Tag> tags = session.createQuery(query, Tag.class).setParameter("name", name).list();
            return tags.stream().findAny();
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
