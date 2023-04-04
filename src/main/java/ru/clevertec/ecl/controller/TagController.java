package ru.clevertec.ecl.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.config.PaginationProperties;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.model.dto.response.ApiResponse;
import ru.clevertec.ecl.model.dto.response.PageResponse;
import ru.clevertec.ecl.model.dto.response.TagDtoResponse;
import ru.clevertec.ecl.service.TagService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static ru.clevertec.ecl.controller.TagController.TAG_API_PATH;
import static ru.clevertec.ecl.model.dto.response.ApiResponse.apiResponseEntity;

/**
 * Tag API
 *
 * @author Konstantin Voytko
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = TAG_API_PATH)
public class TagController {

    private final TagService tagService;
    private final PaginationProperties paginationProperties;

    public static final String TAG_API_PATH = "/api/v0/tags";

    /**
     * POST /api/v0/tags : Create a new Tag
     *
     * @param tagDtoRequest Tag object to create (required)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TagDtoResponse>> createTag(
            @RequestBody @Valid TagDtoRequest tagDtoRequest
    ) {
        TagDtoResponse tag = tagService.createTag(tagDtoRequest);

        return apiResponseEntity(
                "Tag with ID " + tag.getId() + " was created",
                TAG_API_PATH,
                HttpStatus.CREATED,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    /**
     * GET /api/v0/tags : Find Tags info
     *
     * @param page page value to return (not required)
     * @param pageSize page size to return (not required)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TagDtoResponse>>> findAllTags(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        page = Optional.ofNullable(page).orElse(paginationProperties.getDefaultPageValue());
        pageSize = Optional.ofNullable(pageSize).orElse(paginationProperties.getDefaultPageSize());

        PageResponse<TagDtoResponse> tags = tagService.getAllTags(page, pageSize);

        return apiResponseEntity(
                "All Tags",
                TAG_API_PATH,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tags
        );
    }

    /**
     * GET /api/v0/tags/{id} : Find Tag info
     *
     * @param id Tag id to return (required)
     * @throws TagNotFoundException if the Tag with id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDtoResponse>> findTagById(@PathVariable @Valid @NotNull Long id) {
        TagDtoResponse tag = tagService.getTagById(id);

        return apiResponseEntity(
                "Tag with ID " + tag.getId() + " was found",
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    /**
     * PUT /api/v0/tags/{id} : Update an existing Tag info
     *
     * @param id Tag id to return (required)
     * @param tagDtoRequest Tag object to update (required)
     * @throws TagNotFoundException if the Tag with id doesn't exist
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TagDtoResponse>> updateTagById(
            @PathVariable @Valid @NotNull Long id,
            @RequestBody @Valid TagDtoRequest tagDtoRequest
    ) {
        TagDtoResponse tag = tagService.updateTagById(id, tagDtoRequest);

        return apiResponseEntity(
                "Changes were applied to the Tag with ID " + id,
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    /**
     * PATCH /api/v0/tags/{id} : Partial Update an existing Tag info
     *
     * @param id Tag id to return (required)
     * @param tagDtoRequest Tag object to update (required)
     * @throws TagNotFoundException if Tag with id doesn't exist
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<TagDtoResponse>> updateTagByIdPartially(
            @PathVariable @Valid @NotNull Long id,
            @RequestBody TagDtoRequest tagDtoRequest
    ) {
        TagDtoResponse tag = tagService.updateTagByIdPartially(id, tagDtoRequest);

        return apiResponseEntity(
                "Partial changes were applied to the Tag with ID " + id,
                TAG_API_PATH + "/" + id,
                HttpStatus.OK,
                ApiResponse.Color.SUCCESS,
                tag
        );
    }

    /**
     * DELETE /api/v0/tags/{id} : Delete a Tag
     *
     * @param id Tag id to return (required)
     * @throws TagNotFoundException if the Tag with id doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTagById(@PathVariable @Valid @NotNull Long id) {
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
