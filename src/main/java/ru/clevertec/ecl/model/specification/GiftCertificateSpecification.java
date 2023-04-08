package ru.clevertec.ecl.model.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.entity.Tag;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GiftCertificateSpecification {

    public static Specification<GiftCertificate> matchTagName(String tagName) {
        return ((root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<GiftCertificate> subqueryRoot = subquery.from(GiftCertificate.class);
            Join<Tag, GiftCertificate> join = subqueryRoot.join("tags");

            subquery.select(subqueryRoot.get("id")).where(
                    criteriaBuilder.equal(
                            criteriaBuilder.upper(join.get("name")),
                            tagName.toUpperCase()
                    ));

            return criteriaBuilder.in(root.get("id")).value(subquery);
        });
    }

    public static Specification<GiftCertificate> matchDescription(String description) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.upper(root.get("description")), "%" + description.toUpperCase() + "%"));
    }
}
