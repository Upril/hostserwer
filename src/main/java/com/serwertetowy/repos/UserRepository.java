package com.serwertetowy.repos;

import com.serwertetowy.entities.User;
import com.serwertetowy.services.UserSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    UserSummary findByEmail(String email);
    @Query(nativeQuery = true, value = """
            select
                id, first_name as firstname, last_name as lastname, email
            from users
            order by id""")
    List<UserSummary> findAllUserData();
    User findByEmailAndPassword(String email, String password);
}
