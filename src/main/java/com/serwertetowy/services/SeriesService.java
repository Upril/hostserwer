package com.serwertetowy.services;

import com.serwertetowy.entities.Series;
import com.serwertetowy.services.dto.SeriesSummary;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface SeriesService {
    //method for saving new series info into the db
    Series saveSeries(String name, String description, List<Integer> tagIds) throws IOException;
    Series saveSeriesWithImage(MultipartFile file, String name, String description, List<Integer> tags) throws IOException;
    //methods for gathering data for get request responses
    List<SeriesSummary> getAllSeries();
    SeriesSummary getSeriesById(Integer id);
    //Webflux method to retrieve series icon
    Mono<Resource> getSeriesImageData(Integer id);
    //method allowing the user to add a series into their watchlist, may be moved into user service

}
