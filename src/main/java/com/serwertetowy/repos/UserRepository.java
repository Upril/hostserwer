package com.serwertetowy.repos;

import com.serwertetowy.entities.User;
import com.serwertetowy.services.dto.UserSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    //selective db data retrieval without watchlists and pictures
    UserSummary findByEmail(String email);
    Boolean existsByEmail(String email);
    @Query(nativeQuery = true, value = """
            select
                id, first_name as firstname, last_name as lastname, email, deleted
            from users
            order by id""")
    List<UserSummary> findAllUserData();
}
