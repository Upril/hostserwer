package com.serwertetowy.services;

import com.serwertetowy.services.dto.RatingSummary;

import java.util.List;

public interface RatingService {
    //method that returns a ratingSummary based on rating id
    RatingSummary getRating(Long id);
    //method that returns a list of ratings of given series
    List<RatingSummary> getRatingsBySeries(Long seriesId);
    //method that returns a list of ratings of given user
    List<RatingSummary> getRatingsByUser(Long seriesId);
    //method for saving ratings in the db
    void saveRating(Long userId, Long seriesId,
                    short plotRating, short musicRating, short graphicsRating,
                    short charactersRating, short generalRating);
}
