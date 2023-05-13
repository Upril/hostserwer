package com.serwertetowy.repos;

import com.serwertetowy.entities.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Integer> {
    List<Series> findByNameContainingIgnoreCase(String name);
}
