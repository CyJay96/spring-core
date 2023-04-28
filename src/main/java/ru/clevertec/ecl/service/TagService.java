package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;

public interface TagService {

    TagDtoResponse save(TagDtoRequest tagDtoRequest);

    PageResponse<TagDtoResponse> findAll(Pageable pageable);

    TagDtoResponse findById(Long id);

    TagDtoResponse update(Long id, TagDtoRequest tagDtoRequest);

    TagDtoResponse updatePartially(Long id, TagDtoRequest tagDtoRequest);

    void deleteById(Long id);
}
