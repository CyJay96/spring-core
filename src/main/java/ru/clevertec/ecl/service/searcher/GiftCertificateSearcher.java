package ru.clevertec.ecl.service.searcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.entity.GiftCertificate;
import ru.clevertec.ecl.model.enums.SortType;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GiftCertificateSearcher {

    private final GiftCertificateRepository giftCertificateRepository;

    public List<GiftCertificate> getGiftCertificatesByCriteria(GiftCertificateCriteria searchCriteria) {
        List<GiftCertificate> giftCertificates = new ArrayList<>();

        if (searchCriteria.getTagName() != null) {
            giftCertificates.addAll(giftCertificateRepository.findAllByTagName(searchCriteria.getTagName()));
        }
        if (searchCriteria.getDescription() != null) {
            if (giftCertificates.isEmpty()) {
                giftCertificates.addAll(giftCertificateRepository.findAllLikeDescription(searchCriteria.getDescription()));
            } else {
                giftCertificates = giftCertificates.stream()
                        .filter(giftCertificate -> giftCertificate.getDescription().toLowerCase()
                                .matches(".*" + searchCriteria.getDescription().toLowerCase() + ".*"))
                        .toList();
            }
        }
        if (searchCriteria.getSortTypeName() != null) {
            if (giftCertificates.isEmpty()) {
                if (searchCriteria.getSortTypeName().equals(SortType.DESC)) {
                    giftCertificates = giftCertificateRepository.findAll().stream().sorted(Comparator.comparing(GiftCertificate::getName).reversed()).toList();
                } else {
                    giftCertificates = giftCertificateRepository.findAll().stream().sorted(Comparator.comparing(GiftCertificate::getName)).toList();
                }
            } else {
                if (searchCriteria.getSortTypeName().equals(SortType.DESC)) {
                    giftCertificates = giftCertificates.stream().sorted(Comparator.comparing(GiftCertificate::getName).reversed()).toList();
                } else {
                    giftCertificates = giftCertificates.stream().sorted(Comparator.comparing(GiftCertificate::getName)).toList();
                }
            }
        }
        if (searchCriteria.getSortTypeDate() != null) {
            if (giftCertificates.isEmpty()) {
                if (searchCriteria.getSortTypeDate().equals(SortType.DESC)) {
                    giftCertificates = giftCertificateRepository.findAll().stream().sorted(Comparator.comparing(GiftCertificate::getCreateDate).reversed()).toList();
                } else {
                    giftCertificates = giftCertificateRepository.findAll().stream().sorted(Comparator.comparing(GiftCertificate::getCreateDate)).toList();
                }
            } else {
                if (searchCriteria.getSortTypeDate().equals(SortType.DESC)) {
                    giftCertificates = giftCertificates.stream().sorted(Comparator.comparing(GiftCertificate::getCreateDate).reversed()).toList();
                } else {
                    giftCertificates = giftCertificates.stream().sorted(Comparator.comparing(GiftCertificate::getCreateDate)).toList();
                }
            }
        }

        return giftCertificates;
    }
}
