package ru.clevertec.ecl.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;

import java.util.Properties;

@UtilityClass
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }

        Configuration configuration = new Configuration();

        Properties properties = new Properties();
        properties.put(Environment.URL, "jdbc:postgresql://localhost:5432/gift_certificate");
        properties.put(Environment.DRIVER, "org.postgresql.Driver");
        properties.put(Environment.USER, "postgres");
        properties.put(Environment.PASS, "postgres");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        configuration.setProperties(properties);

        configuration.addAnnotatedClass(GiftCertificate.class);
        configuration.addAnnotatedClass(Tag.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }
}
