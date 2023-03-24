package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.mapper.list.TagListMapper;
import ru.clevertec.ecl.model.dto.TagDto;
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
    public TagDto createTag(TagDto tagDto) {
        Tag savedTag = tagRepository.save(tagMapper.toEntity(tagDto));
        return tagMapper.toDto(savedTag);
    }

    @Override
    public List<TagDto> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tagListMapper.toDto(tags);
    }

    @Override
    public TagDto getTagById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toDto)
                .orElseThrow(() -> new TagNotFoundException(id));
    }

    @Override
    public TagDto updateTagById(Long id, TagDto tagDto) {
        Tag tag = tagMapper.toEntity(tagDto);
        tag.setId(id);

        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toDto(savedTag);
    }

    @Override
    public TagDto updateTagByIdPartially(Long id, TagDto tagDto) {
        Tag updatedTag = tagRepository.findById(id)
                .map(tag -> {
                    Optional.ofNullable(tagDto.getName()).ifPresent(tag::setName);
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
