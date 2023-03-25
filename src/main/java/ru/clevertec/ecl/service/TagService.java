package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;

import java.util.List;

public interface TagService {

    TagDtoResponse createTag(TagDtoRequest tagDtoRequest);

    List<TagDtoResponse> getAllTags();

    TagDtoResponse getTagById(Long id);

    TagDtoResponse updateTagById(Long id, TagDtoRequest tagDtoRequest);

    TagDtoResponse updateTagByIdPartially(Long id, TagDtoRequest tagDtoRequest);

    void deleteTagById(Long id);
}
