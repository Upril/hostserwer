package com.serwertetowy.controllers;

import com.serwertetowy.exceptions.*;
import com.serwertetowy.services.WatchlistService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.serwertetowy.auth.AuthenticationService.getIdentity;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/watchlist")
public class WatchlistController {
    private WatchlistService watchlistService;
    record WatchlistDto(Long id ,SeriesSummary seriesSummary, String watchFlag){}
    private record WatchlistPostRequest(@Min(1) @NotNull(message = "Series id is mandatory") Integer seriesId,
                                        @Min(1) @NotNull(message = "User id is mandatory")Integer userId,
                                        @Min(1) @NotNull(message = "Watchflag id is mandatory")Integer watchflagId){}

    private record WatchlistPutRequest(@Min(value = 1, message = "Invalid series id") @NotNull(message = "Series id is mandatory") Integer seriesId,
                                        @Min(value = 1,message = "Invalid watchflag id") @NotNull(message = "Watchflag id is mandatory")Integer watchflagId,
                                        @NotNull(message = "isFavourite information is mandatory") Boolean isFavourite){}
    @GetMapping("/{id}")
    public ResponseEntity<List<WatchlistController.WatchlistDto>> getUserWatchlist(@PathVariable("id") @Min(1) Long id){
        String auth = getIdentity();
        List<UserSeriesSummary> userSeriesSummaryList = watchlistService.getWatchlist(id, auth);
        List<WatchlistDto> watchlistDtoList = new ArrayList<>();
        //assembling watchlistdto from userseries data
        for(UserSeriesSummary userSeriesSummary: userSeriesSummaryList){
            watchlistDtoList.add(new WatchlistDto(userSeriesSummary.getId() ,userSeriesSummary.getSeriesSummary(), userSeriesSummary.getWatchflag().getName()));
        }
        return new ResponseEntity<>(watchlistDtoList, HttpStatus.OK);
    }
    @PostMapping("/addToWatchlist")
    public ResponseEntity<UserSeriesSummary> addSeriesToWatchlist(@Valid @RequestBody WatchlistPostRequest request) throws UserDeletedException {
        String auth = getIdentity();
        return new ResponseEntity<>(watchlistService.addToWatchlist(request.seriesId, request.userId, request.watchflagId, auth), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<WatchlistController.WatchlistDto> putWatchlistItem(@PathVariable("id") @Min(1) Long id, @Valid @RequestBody WatchlistPutRequest request){
        String auth = getIdentity();
        watchlistService.putWatchlistItem(id,request.seriesId,request.watchflagId, request.isFavourite, auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchlistItem(@PathVariable("id") @Min(1) Long id){
        String auth = getIdentity();
        watchlistService.deleteWatchlistItem(id, auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private Map<String,String> createMessage(Exception ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return errors;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String,String> handleUserNotFoundExceptions(UserNotFoundException ex){
        return createMessage(ex);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserDeletedException.class)
    public Map<String,String> handleUserNotFoundExceptions(UserDeletedException ex){
        return createMessage(ex);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SeriesNotFoundException.class)
    public Map<String,String> handleSeriesNotFoundExceptions(SeriesNotFoundException ex){
        return createMessage(ex);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WatchflagNotFoundException.class)
    public Map<String,String> handleWatchflagNotFoundExceptions(WatchflagNotFoundException ex){
        return createMessage(ex);
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(FailedToAuthenticateException.class)
    public Map<String,String> handleFailedToAuthException(FailedToAuthenticateException ex){
        return createMessage(ex);
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

}
