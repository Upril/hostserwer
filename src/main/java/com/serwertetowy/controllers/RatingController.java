package com.serwertetowy.controllers;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.services.RatingService;
import com.serwertetowy.services.dto.RatingSummary;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    //record to simplify the post request
    record PostRatingRequest(@Min(1) @NotNull Long userId, @Min(1) @NotNull Long seriesId,
                             @Min(1) @Max(10) @NotNull short plotRating, @Min(1) @Max(10) @NotNull short musicRating, @Min(1) @Max(10) @NotNull short graphicsRating,
                             @Min(1) @Max(10) @NotNull short charactersRating, @Min(1) @Max(10) @NotNull short generalRating){}
    record PutRatingRequest(short plotRating, short musicRating, short graphicsRating,
                            short charactersRating, short generalRating){}
    //saving ratings with post requests
    @PostMapping
    public ResponseEntity<Rating> saveRating(@RequestBody @Valid PostRatingRequest request){
        ratingService.saveRating(request.userId, request.seriesId, request.plotRating,
                                request.musicRating, request.graphicsRating,
                                request.charactersRating, request.generalRating);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //update rating data
    @PutMapping("/{id}")
    public ResponseEntity<RatingSummary> putRating(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid PutRatingRequest request){
        return new ResponseEntity<>(ratingService.putRating(id, request.plotRating,
                request.musicRating, request.graphicsRating,
                request.charactersRating, request.generalRating),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable("id") @Min(1) Long id){
        ratingService.deleteRatingById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //GET request for the ratings of given user
    @GetMapping("/user/{id}")
    public ResponseEntity<List<RatingSummary>> getRatingsByUser(@PathVariable("id") @Min(1) Long id){
        return new ResponseEntity<>(ratingService.getRatingsByUser(id), HttpStatus.OK);
    }
    //GET request for rating of given series by different users
    @GetMapping("/series/{id}")
    public ResponseEntity<List<RatingSummary>> getRatingsBySeries(@PathVariable("id") @Min(1) Long id){
        return new ResponseEntity<>(ratingService.getRatingsBySeries(id), HttpStatus.OK);
    }
    //GET request for a given rating
    @GetMapping("/{id}")
    public ResponseEntity<RatingSummary> getRatingById(@PathVariable("id") @Min(1) Long id){
        return new ResponseEntity<>(ratingService.getRating(id), HttpStatus.OK);
    }

}
