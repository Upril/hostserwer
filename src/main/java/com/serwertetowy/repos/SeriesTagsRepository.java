package com.serwertetowy.repos;

import com.serwertetowy.entities.SeriesTags;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeriesTagsRepository extends JpaRepository<SeriesTags, Integer> {
    List<SeriesTags> findBySeriesId(Long seriesId);
    //findbyTag?
}
