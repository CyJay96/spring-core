package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.mapper.list.TagListMapper;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagListMapper tagListMapper;

    @Override
    public TagDtoResponse createTag(TagDtoRequest tagDtoRequest) {
        Tag savedTag = tagRepository.save(tagMapper.toEntity(tagDtoRequest));
        return tagMapper.toDto(savedTag);
    }

    @Override
    public List<TagDtoResponse> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tagListMapper.toDto(tags);
    }

    @Override
    public TagDtoResponse getTagById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toDto)
                .orElseThrow(() -> new TagNotFoundException(id));
    }

    @Override
    public TagDtoResponse updateTagById(Long id, TagDtoRequest tagDtoRequest) {
        Tag tag = tagMapper.toEntity(tagDtoRequest);
        tag.setId(id);

        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toDto(savedTag);
    }

    @Override
    public TagDtoResponse updateTagByIdPartially(Long id, TagDtoRequest tagDtoRequest) {
        Tag updatedTag = tagRepository.findById(id)
                .map(tag -> {
                    Optional.ofNullable(tagDtoRequest.getName()).ifPresent(tag::setName);
                    return tag;
                })
                .orElseThrow(() -> new TagNotFoundException(id));

        Tag savedTag = tagRepository.save(updatedTag);
        return tagMapper.toDto(savedTag);
    }

    @Override
    public void deleteTagById(Long id) {
        try {
            tagRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new TagNotFoundException(id);
        }
    }
}
