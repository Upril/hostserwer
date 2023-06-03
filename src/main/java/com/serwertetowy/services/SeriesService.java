package com.serwertetowy.services;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesSummary;

import java.util.List;
import java.util.Set;

public interface SeriesService {
    Series saveSeries(String name, String description, Set<Tags> tags);
    List<SeriesSummary> getAllSeries();
    SeriesSummary getSeriesById(Integer id);
    UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId);

}
