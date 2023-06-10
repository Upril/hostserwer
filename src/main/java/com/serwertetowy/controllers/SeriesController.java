package com.serwertetowy.controllers;
import com.serwertetowy.entities.Series;
import com.serwertetowy.services.SeriesService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/series")
public class SeriesController {
    private SeriesService seriesService;
    //post series method, a series needs to exist before the episode upload
    @PostMapping
    public ResponseEntity<SeriesSummary> saveSeries(@RequestParam String name,@RequestParam String description,@RequestParam List<Integer> tags, @RequestParam(value = "file", required = false)MultipartFile file) throws IOException {
        Series series;
        if (file == null) series = seriesService.saveSeries(name,description,tags);
        else series = seriesService.saveSeriesWithImage(file,name,description,tags);
        SeriesSummary seriesSummary = seriesService.getSeriesById(series.getId().intValue());
        return new ResponseEntity<>(seriesSummary,HttpStatus.OK);
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
    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Transactional
    public Mono<Resource> getEpisodeImage(@PathVariable Integer id){
        return seriesService.getSeriesImageData(id);
    }
    //post method allowing the user to add a given series into their watchlist, may move it to the user controller
    @PostMapping("/addToWatchlist")
    public ResponseEntity<UserSeriesSummary> addSeriesToWatchlist(@RequestParam Integer seriesId, @RequestParam Integer userId){
        return new ResponseEntity<>(seriesService.addToWatchlist(seriesId,userId), HttpStatus.OK);
    }



}
