package com.serwertetowy.controllers;
import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.EpisodeSummary;
import com.serwertetowy.services.SeriesService;
import com.serwertetowy.services.SeriesSummary;
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
    record SeriesRequest(String name, String description, Set<Tags> tags){}
    record SeriesPutRequest(String name, String description, Set<Tags> tags, Set<EpisodeSummary> episodes){}

    @PostMapping
    public ResponseEntity<Series> saveSeries(@RequestBody SeriesRequest request){
        Series series = seriesService.saveSeries(request.name,request.description,request.tags);
        return new ResponseEntity<>(series, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<SeriesSummary>> getAllSeries(){
        return new ResponseEntity<>(seriesService.getAllSeries(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SeriesSummary> getSeriesById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(seriesService.getSeriesById(id), HttpStatus.OK);
    }




}
