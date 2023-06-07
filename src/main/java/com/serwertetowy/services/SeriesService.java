package com.serwertetowy.services;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;

import java.util.List;
import java.util.Set;

public interface SeriesService {
    //method for saving new series info into the db
    Series saveSeries(String name, String description, Set<Tags> tags);
    //methods for gathering data for get request responses
    List<SeriesSummary> getAllSeries();
    SeriesSummary getSeriesById(Integer id);
    //method allowing the user to add a series into their watchlist, may be moved into user service
    UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId);

}
