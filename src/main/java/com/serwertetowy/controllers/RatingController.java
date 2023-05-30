package com.serwertetowy.controllers;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.services.RatingService;
import com.serwertetowy.services.RatingSummary;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ratings")
@AllArgsConstructor
public class RatingController {
    private RatingService ratingService;
    record PostRatingRequest(Long userId, Long seriesId,
                             short plotRating, short musicRating, short graphicsRating,
                             short charactersRating, short generalRating){}
    @PostMapping
    public ResponseEntity<Rating> saveRating(@RequestBody PostRatingRequest request){
        ratingService.saveRating(request.userId, request.seriesId, request.plotRating,
                                request.musicRating, request.graphicsRating,
                                request.charactersRating, request.generalRating);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<RatingSummary>> getRatingsByUser(@PathVariable("id") Long id){
        return new ResponseEntity<>(ratingService.getRatingsByUser(id), HttpStatus.OK);
    }
    @GetMapping("/series/{id}")
    public ResponseEntity<List<RatingSummary>> getRatingsBySeries(@PathVariable("id") Long id){
        return new ResponseEntity<>(ratingService.getRatingsBySeries(id), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RatingSummary> getRatingById(@PathVariable("id") Long id){
        return new ResponseEntity<>(ratingService.getRating(id), HttpStatus.OK);
    }

}
