package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u.* from users u " +
            "inner join orders o on u.id = o.user_id " +
            "group by u.id " +
            "order by sum(o.final_price) desc " +
            "limit 1",
            nativeQuery = true)
    Optional<User> findByHighestOrderCost();

    Optional<User> findFirstByOrderByIdAsc();

    Optional<User> findFirstByOrderByIdDesc();
}
