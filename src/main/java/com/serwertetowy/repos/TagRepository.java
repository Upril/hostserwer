package com.serwertetowy.repos;

import com.serwertetowy.entities.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tags, Integer> {

}
