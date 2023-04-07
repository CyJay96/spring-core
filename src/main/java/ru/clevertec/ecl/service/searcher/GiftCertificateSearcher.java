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

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GiftCertificateSearcher {

    private final GiftCertificateRepository giftCertificateRepository;

    private final Function<GiftCertificateCriteria, Specification<GiftCertificate>> toSpecification =
            searchCriteria -> {
                Specification<GiftCertificate> specification = null;

                if (searchCriteria.getTagName() != null) {
                    specification = append(specification, GiftCertificateSpecification
                            .matchTagName(searchCriteria.getTagName()));
                }
                if (searchCriteria.getDescription() != null) {
                    specification = append(specification, GiftCertificateSpecification
                            .matchDescription(searchCriteria.getDescription()));
                }

                return specification;
            };

    public Page<GiftCertificate> getGiftCertificatesByCriteria(GiftCertificateCriteria searchCriteria) {
        Sort sort = searchCriteria.getSortDirectionName() != null ? Sort.by(searchCriteria.getSortDirectionName(), "name") :
                searchCriteria.getSortDirectionDate() != null ? Sort.by(searchCriteria.getSortDirectionDate(), "createDate") :
                        Sort.by(Sort.Direction.ASC, "id");
        return toSpecification
                .andThen(specification -> giftCertificateRepository.findAll(
                        specification,
                        PageRequest.of(searchCriteria.getOffset(), searchCriteria.getLimit(), sort)))
                .apply(searchCriteria);
    }

    private <T> Specification<T> append(Specification<T> base, Specification<T> specification) {
        if (base == null) {
            return Specification.where(specification);
        }
        return base.and(specification);
    }
}
