package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.model.entity.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagDtoResponse save(TagDtoRequest tagDtoRequest) {
        Tag savedTag = tagRepository.save(tagMapper.toTag(tagDtoRequest));
        return tagMapper.toTagDtoResponse(savedTag);
    }

    @Override
    public PageResponse<TagDtoResponse> findAll(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        List<TagDtoResponse> tagDtoResponses = tagPage.stream()
                .map(tagMapper::toTagDtoResponse)
                .toList();

        return PageResponse.<TagDtoResponse>builder()
                .content(tagDtoResponses)
                .number(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .numberOfElements(tagDtoResponses.size())
                .build();
    }

    @Override
    public TagDtoResponse findById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toTagDtoResponse)
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));
    }

    @Override
    public TagDtoResponse update(Long id, TagDtoRequest tagDtoRequest) {
        Tag updatedTag = tagRepository.findById(id)
                .map(tag -> {
                    tag.setName(tagDtoRequest.getName());
                    return tag;
                })
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));

        Tag savedTag = tagRepository.save(updatedTag);
        return tagMapper.toTagDtoResponse(savedTag);
    }

    @Override
    public TagDtoResponse updatePartially(Long id, TagDtoRequest tagDtoRequest) {
        Tag updatedTag = tagRepository.findById(id)
                .map(tag -> {
                    tagMapper.updateTag(tagDtoRequest, tag);
                    return tag;
                })
                .orElseThrow(() -> new EntityNotFoundException(Tag.class, id));

        Tag savedTag = tagRepository.save(updatedTag);
        return tagMapper.toTagDtoResponse(savedTag);
    }

    @Override
    public void deleteById(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException(Tag.class, id);
        }
        tagRepository.deleteById(id);
    }
}
