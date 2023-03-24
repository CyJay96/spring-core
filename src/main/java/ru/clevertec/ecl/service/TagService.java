package ru.clevertec.ecl.service;

import ru.clevertec.ecl.model.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto createTag(TagDto tagDto);

    List<TagDto> getAllTags();

    TagDto getTagById(Long id);

    TagDto updateTagById(Long id, TagDto tagDto);

    TagDto updateTagByIdPartially(Long id, TagDto tagDto);

    void deleteTagById(Long id);
}
