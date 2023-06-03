package com.serwertetowy.services;

import com.serwertetowy.services.dto.RatingSummary;

import java.util.List;

public interface RatingService {
    RatingSummary getRating(Long id);
    List<RatingSummary> getRatingsBySeries(Long seriesId);
    List<RatingSummary> getRatingsByUser(Long seriesId);
    void saveRating(Long userId, Long seriesId,
                    short plotRating, short musicRating, short graphicsRating,
                    short charactersRating, short generalRating);
}
