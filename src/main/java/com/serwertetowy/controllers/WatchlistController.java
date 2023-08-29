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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.serwertetowy.auth.AuthenticationService.getIdentity;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    record WatchlistDto(Long id ,SeriesSummary seriesSummary, String watchFlag){}
    private record WatchlistPostRequest(@Min(1) @NotNull(message = "Series id is mandatory") Integer seriesId,
                                        @Min(1) @NotNull(message = "User id is mandatory")Integer userId,
                                        @Min(1) @NotNull(message = "Watchflag id is mandatory")Integer watchflagId){}

    private record WatchlistPutRequest(@Min(value = 1, message = "Invalid series id") @NotNull(message = "Series id is mandatory") Integer seriesId,
                                        @Min(value = 1, message = "Invalid watchflag id") @NotNull(message = "Watchflag id is mandatory") Integer watchflagId,
                                        @NotNull(message = "isFavourite information is mandatory") Boolean isFavourite){}
    @GetMapping("/{id}")
    public ResponseEntity<List<WatchlistDto>> getUserWatchlist(@PathVariable("id") @Min(1) Long id){
        String auth = getIdentity();
        List<UserSeriesSummary> userSeriesSummaryList = watchlistService.getWatchlist(id, auth);
        //assembling watchlistdto from userseries data
        List<WatchlistDto> watchlistDtoList = userSeriesSummaryList.stream().map(userSeriesSummary ->
                new WatchlistDto(
                        userSeriesSummary.getId(),
                        userSeriesSummary.getSeriesSummary(),
                        userSeriesSummary.getWatchflag().getName())
        ).toList();
        //new ArrayList<>();
//        for(UserSeriesSummary userSeriesSummary: userSeriesSummaryList){
//            watchlistDtoList.add(new WatchlistDto(userSeriesSummary.getId() ,userSeriesSummary.getSeriesSummary(), userSeriesSummary.getWatchflag().getName()));
//        }
        return ResponseEntity.ok(watchlistDtoList);
    }
    @PostMapping("/addToWatchlist")
    public ResponseEntity<UserSeriesSummary> addSeriesToWatchlist(@Valid @RequestBody WatchlistPostRequest request) throws UserDeletedException {
        String auth = getIdentity();
        return ResponseEntity.ok(watchlistService.addToWatchlist(request.seriesId, request.userId, request.watchflagId, auth));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> putWatchlistItem(@PathVariable("id") @Min(1) Long id, @Valid @RequestBody WatchlistPutRequest request) {
        String auth = getIdentity();
        watchlistService.putWatchlistItem(id, request.seriesId, request.watchflagId, request.isFavourite, auth);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchlistItem(@PathVariable("id") @Min(1) Long id) {
        String auth = getIdentity();
        watchlistService.deleteWatchlistItem(id, auth);
        return ResponseEntity.ok().build();
    }
    private Map<String, String> createMessage(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            UserNotFoundException.class,
            UserDeletedException.class,
            SeriesNotFoundException.class,
            WatchflagNotFoundException.class,
            FailedToAuthenticateException.class
    })
    public Map<String,String> handleUserNotFoundExceptions(Exception ex) {
        return createMessage(ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String name = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(name, msg);
        });
        return errors;
    }

}
