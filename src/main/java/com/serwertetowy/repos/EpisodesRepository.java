package com.serwertetowy.repos;

import com.serwertetowy.entities.Episodes;
import com.serwertetowy.services.dto.EpisodeSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodesRepository extends JpaRepository<Episodes, Integer> {
    @Deprecated
    List<Episodes> findBySeriesId(Integer seriesId);
    @Query(value = """
            select
                id, title, languages, series_id
            from episodes
            order by id""", nativeQuery = true)
    List<EpisodeSummary> findEpisodeSummaryBySeriesId(@Param("seriesId") Integer seriesId);
    EpisodeSummary findEpisodeSummaryById(@Param("id") Integer id);
}
