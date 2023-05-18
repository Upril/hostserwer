package com.serwertetowy.repos;

import com.serwertetowy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSeriesRepository extends JpaRepository<User, Long> {
}
