package ru.clevertec.ecl.service.searcher;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.model.enums.SortType;
import ru.clevertec.ecl.util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftCertificateSearcher {

    public List<GiftCertificate> getGiftCertificatesByCriteria(GiftCertificateCriteria searchCriteria) {
        List<GiftCertificate> giftCertificates = new ArrayList<>();

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

            List<Predicate> predicates = new ArrayList<>();

            // tag name field
            if (searchCriteria.getTagName() != null) {
                Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
                Root<GiftCertificate> subqueryRoot = subquery.from(GiftCertificate.class);
                Join<Tag, GiftCertificate> join = subqueryRoot.join("tags");

                subquery.select(subqueryRoot.get("id")).where(
                        criteriaBuilder.equal(
                                criteriaBuilder.upper(join.get("name")),
                                searchCriteria.getTagName().toUpperCase()
                        ));

                predicates.add(criteriaBuilder.in(root.get("id")).value(subquery));
            }

            // description field
            if (searchCriteria.getDescription() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
                        "%" + searchCriteria.getDescription().toUpperCase() + "%"));
            }

            criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

            // sort name field
            if (searchCriteria.getSortTypeName() != null) {
                if (searchCriteria.getSortTypeName().equals(SortType.DESC)) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
                }
            }

            // sort createDate field
            if (searchCriteria.getSortTypeDate() != null) {
                if (searchCriteria.getSortTypeName().equals(SortType.DESC)) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createDate")));
                }
            }

            giftCertificates.addAll(session.createQuery(criteriaQuery).getResultList());
        }

        return giftCertificates;
    }
}
