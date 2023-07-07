package com.serwertetowy.controllers;

import com.serwertetowy.services.WatchlistService;
import com.serwertetowy.services.dto.UserSeriesSummary;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/watchlist")
public class WatchlistController {
    private WatchlistService watchlistService;
    @GetMapping("/{id}")
    public ResponseEntity<List<UserController.WatchlistDto>> getUserWatchlist(@PathVariable Long id){
        List<UserSeriesSummary> userSeriesSummaryList = watchlistService.getWatchlist(id);
        List<UserController.WatchlistDto> watchlistDtoList = new ArrayList<>();
        //assembling watchlistdto from userseries data
        for(UserSeriesSummary userSeriesSummary: userSeriesSummaryList){
            watchlistDtoList.add(new UserController.WatchlistDto(userSeriesSummary.getSeriesSummary(), userSeriesSummary.getWatchflag().getName()));
        }
        return new ResponseEntity<>(watchlistDtoList, HttpStatus.OK);
    }
    @PostMapping("/addToWatchlist")
    public ResponseEntity<UserSeriesSummary> addSeriesToWatchlist(@RequestParam Integer seriesId, @RequestParam Integer userId, @RequestParam Integer watchflagId){
        return new ResponseEntity<>(watchlistService.addToWatchlist(seriesId,userId, watchflagId), HttpStatus.OK);
    }
}
