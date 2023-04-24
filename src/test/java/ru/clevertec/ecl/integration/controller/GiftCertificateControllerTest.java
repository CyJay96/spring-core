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
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateCriteriaTestBuilder;
import ru.clevertec.ecl.builder.giftCertificate.GiftCertificateDtoRequestTestBuilder;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.criteria.GiftCertificateCriteria;
import ru.clevertec.ecl.model.dto.request.GiftCertificateDtoRequest;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.ecl.controller.GiftCertificateController.GIFT_CERTIFICATE_API_PATH;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GiftCertificateControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;

    private final GiftCertificateDtoRequest giftCertificateDtoRequest = GiftCertificateDtoRequestTestBuilder
            .aGiftCertificateDtoRequest()
            .build();
    private final GiftCertificateCriteria searchCriteria = GiftCertificateCriteriaTestBuilder
            .aGiftCertificateCriteria()
            .withDescription("frame")
            .build();

    @Test
    @DisplayName("Create Gift Certificate")
    void checkCreateGiftCertificateShouldReturnGiftCertificateDtoResponse() throws Exception {
        long expectedGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1;
        mockMvc.perform(post(GIFT_CERTIFICATE_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(giftCertificateDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(expectedGiftCertificateId));
    }

    @Test
    @DisplayName("Find all Gift Certificates")
    void checkFindAllGiftCertificatesShouldReturnGiftCertificateDtoResponsePage() throws Exception {
        int expectedGiftCertificatesSize = (int) giftCertificateRepository.count();
        mockMvc.perform(get(GIFT_CERTIFICATE_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(PAGE))
                        .param("pageSize", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedGiftCertificatesSize));
    }

    @Test
    @DisplayName("Find all Gift Certificates by criteria")
    void checkFindAllGiftCertificatesByCriteriaShouldReturnGiftCertificateDtoResponsePage() throws Exception {
        int expectedGiftCertificatesSize = 2;
        mockMvc.perform(get(GIFT_CERTIFICATE_API_PATH + "/criteria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchCriteria))
                        .param("page", String.valueOf(PAGE))
                        .param("pageSize", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedGiftCertificatesSize));
    }

    @Nested
    public class FindGiftCertificateByIdTest {
        @DisplayName("Find Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) throws Exception {
            mockMvc.perform(get(GIFT_CERTIFICATE_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Find Gift Certificate by ID; not found")
        void checkFindGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() throws Exception {
            long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(GIFT_CERTIFICATE_API_PATH + "/{id}", doesntExistGiftCertificateId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class UpdateGiftCertificateByIdTest {
        @DisplayName("Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) throws Exception {
            mockMvc.perform(put(GIFT_CERTIFICATE_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @DisplayName("Partial Update Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkUpdateGiftCertificateByIdPartiallyShouldReturnGiftCertificateDtoResponse(Long id) throws Exception {
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Partial Update Gift Certificate by ID; not found")
        void checkUpdateGiftCertificateByIdPartiallyShouldThrowGiftCertificateNotFoundException() throws Exception {
            long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/{id}", doesntExistGiftCertificateId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateDtoRequest)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class AddTagToGiftCertificateTest {
        @Test
        @DisplayName("Add Tag to Gift Certificate")
        void checkAddTagToGiftCertificateShouldReturnGiftCertificateDtoResponse() throws Exception {
            long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/add/{giftCertificateId}/{tagId}",
                            existsGiftCertificateId, existsTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(existsGiftCertificateId));
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Gift Certificate not found")
        void checkAddTagToGiftCertificateShouldThrowGiftCertificateNotFoundException() throws Exception {
            long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/add/{giftCertificateId}/{tagId}",
                            doesntExistGiftCertificateId, existsTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Add Tag to Gift Certificate; Tag not found")
        void checkAddTagToGiftCertificateShouldThrowTagNotFoundException() throws Exception {
            long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/add/{giftCertificateId}/{tagId}",
                            existsGiftCertificateId, doesntExistTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class DeleteTagFromGiftCertificateTest {
        @Test
        @DisplayName("Delete Tag from Gift Certificate")
        void checkDeleteTagFromGiftCertificateShouldReturnGiftCertificateDtoResponse() throws Exception{
            long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/delete/{giftCertificateId}/{tagId}",
                            existsGiftCertificateId, existsTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(existsGiftCertificateId));
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Gift Certificate not found")
        void checkDeleteTagFromGiftCertificateShouldThrowGiftCertificateNotFoundException() throws Exception {
            long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            long existsTagId = tagRepository.findFirstByOrderByIdAsc().get().getId();
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/delete/{giftCertificateId}/{tagId}",
                            doesntExistGiftCertificateId, existsTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Delete Tag from Gift Certificate; Tag not found")
        void checkDeleteTagFromGiftCertificateShouldThrowTagNotFoundException() throws Exception {
            long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            long doesntExistTagId = new Random()
                    .nextLong(tagRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(patch(GIFT_CERTIFICATE_API_PATH + "/delete/{giftCertificateId}/{tagId}",
                            existsGiftCertificateId, doesntExistTagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class DeleteGiftCertificateByIdTest {
        @DisplayName("Delete Gift Certificate by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkDeleteGiftCertificateByIdShouldReturnGiftCertificateDtoResponse(Long id) throws Exception {
            mockMvc.perform(delete(GIFT_CERTIFICATE_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Delete Gift Certificate by ID; not found")
        void checkDeleteGiftCertificateByIdShouldThrowGiftCertificateNotFoundException() throws Exception {
            long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(delete(GIFT_CERTIFICATE_API_PATH + "/{id}", doesntExistGiftCertificateId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
