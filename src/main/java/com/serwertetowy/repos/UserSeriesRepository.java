package com.serwertetowy.repos;

import com.serwertetowy.entities.UserSeries;
import com.serwertetowy.services.dto.UserSeriesData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSeriesRepository extends JpaRepository<UserSeries, Long> {
    //retriving specific db info about the watchlists
    List<UserSeriesData> findByUserId(Long id);
    //get info of a specific item in the user watchlist
    UserSeriesData findByUserIdAndSeriesId(Long userId, Long seriesId);
}
