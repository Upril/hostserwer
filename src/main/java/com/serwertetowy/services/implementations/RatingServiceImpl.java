package com.serwertetowy.services.implementations;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.User;
import com.serwertetowy.exceptions.FailedToAuthenticateException;
import com.serwertetowy.exceptions.RatingNotFoundException;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.exceptions.UserNotFoundException;
import com.serwertetowy.repos.RatingRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.repos.UserRepository;
import com.serwertetowy.services.RatingService;
import com.serwertetowy.services.dto.RatingSummary;
import com.serwertetowy.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {
    private RatingRepository ratingRepository;
    private UserService userService;
    private SeriesRepository seriesRepository;
    private UserRepository userRepository;
    @Override
    public RatingSummary getRating(Long id) {
        return ratingRepository.findById(id);
    }

    @Override
    public RatingSummary putRating(Long id, short plotRating, short musicRating, short graphicsRating, short charactersRating, short generalRating, String auth) {
        if(!ratingRepository.existsById(id.intValue())) throw new RatingNotFoundException();
        Rating rating = ratingRepository.findById(id.intValue())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = rating.getUser();
        if(!Objects.equals(user.getEmail(), auth)) throw new FailedToAuthenticateException();
        rating.setPlotRating(plotRating);
        rating.setMusicRating(musicRating);
        rating.setGraphicsRating(graphicsRating);
        rating.setCharactersRating(charactersRating);
        rating.setGeneralRating(generalRating);
        ratingRepository.save(rating);
        return ratingRepository.findById(id);
    }

    @Override
    public void deleteRatingById(Long id, String auth) {
        if(!ratingRepository.existsById(id.intValue())) throw new RatingNotFoundException();
        Rating rating = ratingRepository.findById(id.intValue())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = rating.getUser();
        if(!Objects.equals(user.getEmail(), auth)) throw new FailedToAuthenticateException();
        ratingRepository.delete(rating);
    }

    @Override
    public List<RatingSummary> getRatingsBySeries(Long seriesId) {
        if(!seriesRepository.existsById(seriesId.intValue())) throw new SeriesNotFoundException();
        return ratingRepository.findBySeriesId(seriesId);
    }

    @Override
    public List<RatingSummary> getRatingsByUser(Long userId) {
        if(!userRepository.existsById(userId)) throw new UserNotFoundException();
        return ratingRepository.findByUserId(userId);
    }

    @Override
    public void saveRating(Long userId, Long seriesId, short plotRating, short musicRating, short graphicsRating, short charactersRating, short generalRating, String auth) {
        if(!userRepository.existsById(userId)) throw new UserNotFoundException();
        if(!seriesRepository.existsById(seriesId.intValue())) throw new SeriesNotFoundException();
        User user = userService.getUserById(userId);
        if(!Objects.equals(user.getEmail(), auth)) throw new FailedToAuthenticateException();
        //assemble rating data - get series information
        Series series = seriesRepository.findById(seriesId.intValue())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        Rating rating = new Rating(series,user,plotRating,musicRating,graphicsRating,charactersRating,generalRating);
        ratingRepository.save(rating);
    }
}
