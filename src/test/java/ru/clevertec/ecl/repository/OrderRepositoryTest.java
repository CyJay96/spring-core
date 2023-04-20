//package ru.clevertec.ecl.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import ru.clevertec.ecl.builder.order.OrderTestBuilder;
//import ru.clevertec.ecl.builder.user.UserTestBuilder;
//import ru.clevertec.ecl.model.entity.Order;
//import ru.clevertec.ecl.model.entity.User;
//
//import java.util.List;
//
//import static ru.clevertec.ecl.util.TestConstants.PAGE_SIZE;
//import static org.assertj.core.api.Assertions.assertThat;
//import static ru.clevertec.ecl.util.TestConstants.TEST_ID;
//
//@SpringBootTest
//@PropertySource(value = "classpath:application-test.yml")
//class OrderRepositoryTest {
//
//    private static final int TEST_PAGE_NUMBER = 0;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//
//    private final OrderTestBuilder orderTestBuilder = OrderTestBuilder.aOrder();
//    private final UserTestBuilder userTestBuilder = UserTestBuilder.aUser();
//
//    @BeforeEach
//    public void init() {
//    }
//
//    @Test
//    void itShouldFindAllByUserId() {
//        User user = userTestBuilder.build();
//        Order expectedOrder = orderTestBuilder.build();
//
////        user.setOrders(List.of(expectedOrder));
////        userRepository.save(user); //todo check order save when user saved, otherwise save order too
//
//        PageRequest pageRequest = PageRequest.of(TEST_PAGE_NUMBER, PAGE_SIZE);
//        Page<Order> actualUsersById = orderRepository.findAllByUserId(user.getId(), pageRequest);
//
//        System.out.println(actualUsersById.getContent());
//
//        assertThat(actualUsersById.getContent().stream()
//                .anyMatch(order -> order.getUser().equals(expectedOrder.getUser()))
//        ).isTrue();
//    }
//
//    @Test
//    @DisplayName("Find Order by ID & User ID")
//    void checkFindByIdAndUserIdShouldReturnUserDtoResponse() {
//    }
//}
