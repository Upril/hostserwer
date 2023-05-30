package com.serwertetowy.services;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.repos.RatingRepository;
import com.serwertetowy.repos.SeriesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService{
    private RatingRepository ratingRepository;
    private UserService userService;
    private SeriesRepository seriesRepository;
    @Override
    public RatingSummary getRating(Long id) {
        return ratingRepository.findById(id);
    }

    @Override
    public List<RatingSummary> getRatingsBySeries(Long seriesId) {
        return ratingRepository.findBySeriesId(seriesId);
    }

    @Override
    public List<RatingSummary> getRatingsByUser(Long userId) {
        return ratingRepository.findByUserId(userId);
    }

    @Override
    public void saveRating(Long userId, Long seriesId, short plotRating, short musicRating, short graphicsRating, short charactersRating, short generalRating) {
        User user = userService.getUserById(userId);
        Optional<Series> series = seriesRepository.findById(seriesId.intValue());
        if(series.isEmpty()) throw new SeriesNotFoundException();//may delete later
        Series seriesFr = series.get();
        Rating rating = new Rating(seriesFr,user,plotRating,musicRating,graphicsRating,charactersRating,generalRating);
        ratingRepository.save(rating);
    }
}
