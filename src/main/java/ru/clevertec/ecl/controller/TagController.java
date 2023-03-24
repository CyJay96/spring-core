package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.model.dto.TagDto;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

import static ru.clevertec.ecl.controller.TagController.TAG_API_PATH;
import static ru.clevertec.ecl.model.dto.response.ApiResponse.apiResponseEntity;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = TAG_API_PATH)
public class TagController {

    private final TagService tagService;

    public static final String TAG_API_PATH = "/api/v0/tags";

    @PostMapping
    public ResponseEntity<ApiResponse<TagDto>> createTag(
            @RequestBody @Valid TagDto tagDto
    ) {
        TagDto tag = tagService.createTag(tagDto);

        return apiResponseEntity(
                "Tag with ID " + tag.getId() + " was created",
                TAG_API_PATH,
                HttpStatus.CREATED,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagDto>>> findAllTags() {
        List<TagDto> tags = tagService.getAllTags();

        return apiResponseEntity(
                "All Tags",
                TAG_API_PATH,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tags
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDto>> findTagById(@PathVariable @Valid @NotNull Long id) {
        TagDto tag = tagService.getTagById(id);

        return apiResponseEntity(
                "Tag with ID " + tag.getId() + " was found",
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TagDto>> updateTagById(
            @PathVariable @Valid @NotNull Long id,
            @RequestBody @Valid TagDto tagDto
    ) {
        TagDto tag = tagService.updateTagById(id, tagDto);

        return apiResponseEntity(
                "Changes were applied to the Tag with ID " + id,
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TagDto>> updateGiftCertificateByIdPartially(
            @PathVariable @Valid @NotNull Long id,
            @RequestBody TagDto tagDto
    ) {
        TagDto tag = tagService.updateTagByIdPartially(id, tagDto);

        return apiResponseEntity(
                "Partial changes were applied to the Tag with ID " + id,
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGiftCertificateById(@PathVariable @Valid @NotNull Long id) {
        tagService.deleteTagById(id);

        return apiResponseEntity(
                "Tag with ID " + id + " was deleted",
                TAG_API_PATH + "/" + id,
                HttpStatus.NO_CONTENT,
                ApiResponse.Color.SUCCESS,
                null
        );
    }
}
