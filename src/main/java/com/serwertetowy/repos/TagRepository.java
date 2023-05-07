package com.serwertetowy.repos;

import com.serwertetowy.entities.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tags, Integer> {
    public List<Tags> findAllByOrderByIdAsc();
}
