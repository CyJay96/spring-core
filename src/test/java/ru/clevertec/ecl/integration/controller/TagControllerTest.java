package ru.clevertec.ecl.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.builder.tag.TagDtoRequestTestBuilder;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.dto.request.TagDtoRequest;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.ecl.controller.TagController.TAG_API_PATH;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TagControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TagRepository tagRepository;

    private final TagDtoRequest tagDtoRequest = TagDtoRequestTestBuilder.aTagDtoRequest().build();

    @Test
    @DisplayName("Save Tag")
    void checkSaveShouldReturnTagDtoResponse() throws Exception {
        mockMvc.perform(post(TAG_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(tagDtoRequest.getName()));
    }

    @Test
    @DisplayName("Find all Tags")
    void checkFindAllShouldReturnTagDtoResponsePage() throws Exception {
        int expectedTagsSize = (int) tagRepository.count();
        mockMvc.perform(get(TAG_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(PAGE))
                        .param("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedTagsSize));
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnTagDtoResponse(Long id) throws Exception {
            mockMvc.perform(get(TAG_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Find Tag by ID; not found")
        void checkFindByIdShouldThrowTagNotFoundException() throws Exception {
            long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(TAG_API_PATH + "/{id}", doesntExistTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class UpdateByIdTest {
        @DisplayName("Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateShouldReturnTagDtoResponse(Long id) throws Exception {
            mockMvc.perform(put(TAG_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @DisplayName("Partial Update Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdatePartiallyShouldReturnTagDtoResponse(Long id) throws Exception {
            mockMvc.perform(patch(TAG_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Update Tag by ID; not found")
        void checkUpdateShouldThrowTagNotFoundException() throws Exception {
            long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(put(TAG_API_PATH + "/{id}", doesntExistTagId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagDtoRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Partial Update Tag by ID; not found")
        void checkUpdatePartiallyShouldThrowTagNotFoundException() throws Exception {
            long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(patch(TAG_API_PATH + "/{id}", doesntExistTagId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagDtoRequest)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class DeleteByIdTest {
        @DisplayName("Delete Tag by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteByIdShouldReturnVoid(Long id) throws Exception {
            mockMvc.perform(delete(TAG_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Delete Tag by ID; not found")
        void checkDeleteByIdShouldThrowTagNotFoundException() throws Exception {
            long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(delete(TAG_API_PATH + "/{id}", doesntExistTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
