package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;

public interface TagService {

    TagDtoResponse createTag(TagDtoRequest tagDtoRequest);

    PageResponse<TagDtoResponse> getAllTags(Integer page, Integer pageSize);

    TagDtoResponse getTagById(Long id);

    TagDtoResponse updateTagById(Long id, TagDtoRequest tagDtoRequest);

    TagDtoResponse updateTagByIdPartially(Long id, TagDtoRequest tagDtoRequest);

    void deleteTagById(Long id);
}
