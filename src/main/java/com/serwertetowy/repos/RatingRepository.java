package com.serwertetowy.repos;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.services.RatingSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<RatingSummary> findBySeriesId(Long id);
    List<RatingSummary> findByUserId(Long id);
    RatingSummary findById(Long id);
}
