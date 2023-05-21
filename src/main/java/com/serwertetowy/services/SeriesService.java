package com.serwertetowy.services;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.Tags;

import java.util.List;
import java.util.Set;

public interface SeriesService {
    Series saveSeries(String name, String description, Set<Tags> tags);
    List<SeriesSummary> getAllSeries();
    SeriesSummary getSeriesById(Integer id);

}
