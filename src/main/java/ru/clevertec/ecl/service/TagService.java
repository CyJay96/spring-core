package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;

public interface TagService {

    TagDtoResponse save(TagDtoRequest tagDtoRequest);

    PageResponse<TagDtoResponse> findAll(Integer page, Integer pageSize);

    TagDtoResponse findById(Long id);

    TagDtoResponse update(Long id, TagDtoRequest tagDtoRequest);

    TagDtoResponse updatePartially(Long id, TagDtoRequest tagDtoRequest);

    void deleteById(Long id);
}
