package com.serwertetowy.repos;

import com.serwertetowy.entities.Episodes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodesRepository extends JpaRepository<Episodes, Integer> {
    List<Episodes> findBySeriesId(Integer seriesId);
}
