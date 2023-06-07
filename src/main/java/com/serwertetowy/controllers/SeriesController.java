package com.serwertetowy.controllers;
import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.services.SeriesService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/series")
public class SeriesController {
    private SeriesService seriesService;
    //record simplifying the post request
    record SeriesRequest(String name, String description, Set<Tags> tags){}
    //post series method, a series needs to exist before the episode upload
    @PostMapping
    public ResponseEntity<Series> saveSeries(@RequestBody SeriesRequest request){
        Series series = seriesService.saveSeries(request.name,request.description,request.tags);
        return new ResponseEntity<>(series, HttpStatus.OK);
    }
    //get request for all series info without the episode video including detailed tag information
    @GetMapping
    public ResponseEntity<List<SeriesSummary>> getAllSeries(){
        return new ResponseEntity<>(seriesService.getAllSeries(), HttpStatus.OK);
    }
    //get request for given series info without the episode video including detailed tag information
    @GetMapping("/{id}")
    public ResponseEntity<SeriesSummary> getSeriesById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(seriesService.getSeriesById(id), HttpStatus.OK);
    }
    //post method allowing the user to add a given series into their watchlist, may move it to the user controller
    @PostMapping("/addToWatchlist")
    public ResponseEntity<UserSeriesSummary> addSeriesToWatchlist(@RequestParam Integer seriesId, @RequestParam Integer userId){
        return new ResponseEntity<>(seriesService.addToWatchlist(seriesId,userId), HttpStatus.OK);
    }



}
