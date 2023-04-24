//package ru.clevertec.ecl.repository;
//
//import config.PostgresResource;
//import org.junit.jupiter.api.BeforeAll;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//
//@DataJpaTest
////@ActiveProfiles("test-containers-flyway") //todo try to set profile
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
////@Import(TestConfig.class) //todo mb add beans (TestConfig.class)
//public class DaoRepositoryBaseTest {
//
//    @Container
//    public static PostgreSQLContainer<PostgresResource> postgreSQLContainer =
//      PostgresResource.getInstance();
//
//    @BeforeAll
//    public static void start() {
//        postgreSQLContainer.start();
//    }
//}
