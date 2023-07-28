package com.serwertetowy.controllers;

import com.serwertetowy.entities.Rating;
import com.serwertetowy.exceptions.RatingNotFoundException;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.exceptions.UserNotFoundException;
import com.serwertetowy.services.RatingService;
import com.serwertetowy.services.dto.RatingSummary;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/ratings")
@AllArgsConstructor
public class RatingController {
    private RatingService ratingService;
    //record to simplify the post request
    record PostRatingRequest(@Min(1) @NotNull(message = "User id is mandatory") Long userId, @Min(1) @NotNull(message = "Series id is mandatory") Long seriesId,
                             @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                             @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                             @NotNull short plotRating,
                             @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                             @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                             @NotNull short musicRating,
                             @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                             @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                             @NotNull short graphicsRating,
                             @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                             @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                             @NotNull short charactersRating,
                             @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                             @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                             @NotNull short generalRating){}
    record PutRatingRequest(@Min(value = 1,message = "Values of ratings must be between 1 and 10")
                            @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                            @NotNull short plotRating,
                            @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                            @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                            @NotNull short musicRating,
                            @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                            @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                            @NotNull short graphicsRating,
                            @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                            @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                            @NotNull short charactersRating,
                            @Min(value = 1,message = "Values of ratings must be between 1 and 10")
                            @Max(value = 10,message = "Values of ratings must be between 1 and 10")
                            @NotNull short generalRating){}
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

    private Map<String,String> messageCreator(Exception ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String name = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(name,msg);
        });
        return errors;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String,String> handleConstraintExceptions(ConstraintViolationException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String name = String.valueOf(error.getPropertyPath());
            String msg =  error.getMessage();
            errors.put(name,msg);
        });
        return errors;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String,String> handleUserNotFoundExceptions(UserNotFoundException ex){
        return messageCreator(ex);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SeriesNotFoundException.class)
    public Map<String,String> handleSeriesNotFoundExceptions(SeriesNotFoundException ex){return messageCreator(ex);}
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RatingNotFoundException.class)
    public Map<String,String> handleRatingNotFoundExceptions(RatingNotFoundException ex){return messageCreator(ex);}

}
