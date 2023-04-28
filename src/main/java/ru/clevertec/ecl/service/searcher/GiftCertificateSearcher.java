package ru.clevertec.ecl.service.searcher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.specification.GiftCertificateSpecification;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GiftCertificateSearcher {

    private static final String GIFT_CERTIFICATE_ID_FIELD = "id";
    private static final String GIFT_CERTIFICATE_NAME_FIELD = "name";
    private static final String GIFT_CERTIFICATE_CREATE_DATE_FIELD = "createDate";

    private final GiftCertificateRepository giftCertificateRepository;

    private final Function<GiftCertificateCriteria, Specification<GiftCertificate>> toSpecification =
            searchCriteria -> {
                AtomicReference<Specification<GiftCertificate>> specification = new AtomicReference<>();

                if (Objects.nonNull(searchCriteria.getTagNames())) {
                    searchCriteria.getTagNames().forEach(tagName -> {
                        specification.set(append(specification.get(), GiftCertificateSpecification
                                .matchTagName(tagName)));
                    });
                }
                if (Objects.nonNull(searchCriteria.getDescription())) {
                    specification.set(append(specification.get(), GiftCertificateSpecification
                            .matchDescription(searchCriteria.getDescription())));
                }

                return specification.get();
            };

    public Page<GiftCertificate> getGiftCertificatesByCriteria(GiftCertificateCriteria searchCriteria) {
        Sort sort = Objects.nonNull(searchCriteria.getSortDirectionName()) ?
                Sort.by(searchCriteria.getSortDirectionName(), GIFT_CERTIFICATE_NAME_FIELD) :
                Objects.nonNull(searchCriteria.getSortDirectionDate()) ?
                        Sort.by(searchCriteria.getSortDirectionDate(), GIFT_CERTIFICATE_CREATE_DATE_FIELD) :
                        Sort.by(Sort.Direction.ASC, GIFT_CERTIFICATE_ID_FIELD);
        return toSpecification
                .andThen(specification -> giftCertificateRepository.findAll(
                        specification,
                        PageRequest.of(searchCriteria.getOffset(), searchCriteria.getLimit(), sort)))
                .apply(searchCriteria);
    }

    private <T> Specification<T> append(Specification<T> base, Specification<T> specification) {
        if (Objects.isNull(base)) {
            return Specification.where(specification);
        }
        return base.and(specification);
    }
}
