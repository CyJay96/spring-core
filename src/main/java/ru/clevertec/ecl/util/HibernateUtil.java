package ru.clevertec.ecl.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;

@UtilityClass
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }

        Configuration configuration = new Configuration().configure();

        configuration.addAnnotatedClass(GiftCertificate.class);
        configuration.addAnnotatedClass(Tag.class);

        sessionFactory = configuration.buildSessionFactory();

        return sessionFactory;
    }
}
