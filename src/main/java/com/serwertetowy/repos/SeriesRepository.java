package com.serwertetowy.repos;
import com.serwertetowy.entities.Series;
import com.serwertetowy.services.dto.SeriesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Integer>{
    List<Series> findByNameContainingIgnoreCase(String name);
    @Query(value = """
            SELECT series.id, series.description, series.name
            	FROM series
            	order by id""", nativeQuery = true)
    List<SeriesData> findAllData();
    SeriesData findSeriesDataById(@Param("id") Integer id);
}
