package com.serwertetowy.repos;

import com.serwertetowy.entities.UserSeries;
import com.serwertetowy.services.dto.UserSeriesData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSeriesRepository extends JpaRepository<UserSeries, Long> {
    List<UserSeriesData> findByUserId(Long id);
    UserSeriesData findByUserIdAndSeriesId(Long userId, Long seriesId);
}
