package ru.clevertec.ecl.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.model.entity.Order;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.ecl.controller.OrderController.ORDER_API_PATH;
import static ru.clevertec.ecl.util.TestConstants.PAGE;
import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Nested
    public class CreateOrderTest {
        @Test
        @DisplayName("Create Order")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldReturnOrderDtoResponse() throws Exception {
            long expectedUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            long expectedGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            mockMvc.perform(post(ORDER_API_PATH + "/{userId}/{giftCertificateId}",
                            expectedUserId, expectedGiftCertificateId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.userId").value(expectedUserId))
                    .andExpect(jsonPath("$.data.giftCertificateId").value(expectedGiftCertificateId));
        }

        @Test
        @DisplayName("Create Order; User not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowUserNotFoundException() throws Exception {
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            long existsGiftCertificateId = giftCertificateRepository.findFirstByOrderByIdAsc().get().getId();
            mockMvc.perform(post(ORDER_API_PATH + "/{userId}/{giftCertificateId}",
                            doesntExistUserId, existsGiftCertificateId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Create Order; Gift Certificate not found")
        void checkCreateOrderByUserIdAndGiftCertificateIdShouldThrowOrderNotFoundException() throws Exception {
            long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            long doesntExistGiftCertificateId = new Random()
                    .nextLong(giftCertificateRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(post(ORDER_API_PATH + "/{userId}/{giftCertificateId}",
                            existsUserId, doesntExistGiftCertificateId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("Find all Orders")
    void checkFindAllOrdersShouldReturnOrderDtoResponseList() throws Exception {
        int expectedOrdersSize = (int) orderRepository.count();
        mockMvc.perform(get(ORDER_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(PAGE))
                        .param("pageSize", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isNotEmpty())
                .andExpect(jsonPath("$.data.content.size()").value(expectedOrdersSize));
    }

    @Nested
    public class FindAllOrdersByUserIdTest {
        @Test
        @DisplayName("Find all Orders by User ID")
        void checkFindAllOrdersByUserIdShouldReturnOrderDtoResponseList() throws Exception {
            Long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            int expectedOrdersSize = orderRepository
                    .findAllByUserId(existsUserId, PageRequest.of(PAGE, PAGE_SIZE)).getNumberOfElements();
            mockMvc.perform(get(ORDER_API_PATH + "/byUserId/{userId}", existsUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", String.valueOf(PAGE))
                            .param("pageSize", String.valueOf(PAGE_SIZE)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.size()").value(expectedOrdersSize));
        }

        @Test
        @DisplayName("Find all Orders by User ID; User not found")
        void checkFindAllOrdersByUserIdShouldThrowUserNotFoundException() throws Exception {
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(ORDER_API_PATH + "/byUserId/{userId}", doesntExistUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class FindOrderByIdTest {
        @DisplayName("Get Order by ID")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void checkFindOrderByIdShouldReturnOrderDtoResponse(Long id) throws Exception {
            mockMvc.perform(get(ORDER_API_PATH + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(id));
        }

        @Test
        @DisplayName("Find Order by ID; Order not found")
        void checkFindAllOrdersByUserIdShouldThrowUserNotFoundException() throws Exception {
            long doesntExistOrderId = new Random()
                    .nextLong(orderRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(ORDER_API_PATH + "/{id}", doesntExistOrderId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    public class FindOrderByIdAndUserIdTest {
        @DisplayName("Find Order by ID & User ID")
        @Test
        void checkFindOrderByIdAndUserIdShouldReturnOrderDtoResponse() throws Exception {
            Order existsOrder = orderRepository.findFirstByOrderByIdAsc().get();
            long existsOrderId = existsOrder.getId();
            long existsUserId = existsOrder.getUser().getId();
            mockMvc.perform(get(ORDER_API_PATH + "/{orderId}/{userId}", existsOrderId, existsUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.id").value(existsOrderId))
                    .andExpect(jsonPath("$.data.userId").value(existsUserId));
        }

        @Test
        @DisplayName("Find Order by ID & User ID; User not found")
        void checkFindOrderByIdAndUserIdShouldThrowUserNotFoundException() throws Exception {
            long existsOrderId = orderRepository.findFirstByOrderByIdAsc().get().getId();
            long doesntExistUserId = new Random()
                    .nextLong(userRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            mockMvc.perform(get(ORDER_API_PATH + "/{orderId}/{userId}", existsOrderId, doesntExistUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order not found")
        void checkFindOrderByIdAndUserIdShouldThrowOrderNotFoundException() throws Exception {
            long doesntExistOrderId = new Random()
                    .nextLong(orderRepository.findFirstByOrderByIdDesc().get().getId() + 1, Long.MAX_VALUE);
            long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId();
            mockMvc.perform(get(ORDER_API_PATH + "/{orderId}/{userId}", doesntExistOrderId, existsUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Find Order by ID & User ID; Order by User not found")
        void checkFindOrderByIdAndUserIdShouldThrowOrderByUserNotFoundException() throws Exception {
            long existsOrderId = orderRepository.findFirstByOrderByIdAsc().get().getId();
            long existsUserId = userRepository.findFirstByOrderByIdAsc().get().getId() + 1;
            mockMvc.perform(get(ORDER_API_PATH + "/{orderId}/{userId}", existsOrderId, existsUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
