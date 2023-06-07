package com.serwertetowy.repos;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.services.dto.RatingSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<RatingSummary> findBySeriesId(Long id);
    List<RatingSummary> findByUserId(Long id);
    //selective db info retrieval
    RatingSummary findById(Long id);
}
