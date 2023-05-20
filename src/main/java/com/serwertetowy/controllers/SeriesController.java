package com.serwertetowy.controllers;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.SeriesTags;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.repos.SeriesTagsRepository;
import com.serwertetowy.services.EpisodeSummary;
import com.serwertetowy.services.EpisodesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/series")
public class SeriesController {
    private SeriesRepository seriesRepository;
    record SeriesRequest(String name, String description, Set<Tags> tags){}
    record SeriesPutRequest(String name, String description, Set<Tags> tags, Set<EpisodeSummary> episodes){}
    @PostMapping
    public ResponseEntity<Series> saveSeries(@RequestBody SeriesRequest request){
        Set<SeriesTags> seriesTagsSet = new HashSet<>();
        Series series = new Series(request.name,request.description);
        for (Tags tags : request.tags()){
            seriesTagsSet.add(new SeriesTags(series,tags));
        }
        series.setSeriesTags(seriesTagsSet);
        seriesRepository.save(series);
        return new ResponseEntity<>(series, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Series>> getAllSeries(){
        return new ResponseEntity<>(seriesRepository.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable("id") Integer id){
        Optional<Series> series = seriesRepository.findById(id);
        return series.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }




}
