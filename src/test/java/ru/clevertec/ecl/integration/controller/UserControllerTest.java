package ru.clevertec.ecl.integration.controller;

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
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.repository.UserRepository;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.ecl.controller.UserController.USER_API_PATH;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;

    @Test
    @DisplayName("Find all Users")
    void checkFindAllShouldReturnUserDtoResponseList() throws Exception {
        int expectedUsersSize = (int) userRepository.count();
        mockMvc.perform(get(USER_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(PAGE))
                        .param("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedUsersSize));
    }

    @Nested
    public class FindByIdTest {
        @DisplayName("Find User by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindByIdShouldReturnUserDtoResponse(Long id) throws Exception {
            mockMvc.perform(get(USER_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Find User by ID; not found")
        void checkFindByIdShouldThrowTagNotFoundException() throws Exception {
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(USER_API_PATH + "/{id}", doesntExistUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class FindByHighestOrderCostTest {
        @Test
        @DisplayName("Find User by highest order cost")
        void checkFindByHighestOrderCostShouldReturnUserDtoResponse() throws Exception {
            long expectedUserId = userRepository.findByHighestOrderCost().get().getId();
            mockMvc.perform(get(USER_API_PATH + "/highestOrderCost")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(expectedUserId));
        }

        @Test
        @DisplayName("Find User by highest order cost; not found")
        void checkFindByHighestOrderCostShouldThrowTagNotFoundException() throws Exception {
            userRepository.deleteAll();
            mockMvc.perform(get(USER_API_PATH + "/highestOrderCost")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
