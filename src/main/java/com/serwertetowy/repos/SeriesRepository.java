package com.serwertetowy.repos;

import com.serwertetowy.entities.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Integer> {
}
