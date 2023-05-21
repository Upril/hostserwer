package com.serwertetowy.services;

import com.serwertetowy.entities.Series;
import com.serwertetowy.entities.SeriesTags;
import com.serwertetowy.entities.Tags;
import com.serwertetowy.exceptions.TagNotFoundException;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.repos.SeriesTagsRepository;
import com.serwertetowy.repos.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class SeriesServiceImpl implements SeriesService{
    private EpisodesService episodesService;
    private SeriesRepository seriesRepository;
    private SeriesTagsRepository seriesTagsRepository;
    private TagRepository tagRepository;
    @Override
    public Series saveSeries(String name, String description, Set<Tags> tags) {
        Set<SeriesTags> seriesTagsSet = new HashSet<>();
        Series series = new Series(name,description);
        for (Tags tag : tags){
            seriesTagsSet.add(new SeriesTags(series,tag));
        }
        series.setSeriesTags(seriesTagsSet);
        seriesRepository.save(series);
        return series;
    }

    @Override
    public List<SeriesSummary> getAllSeries() {
        List<SeriesSummary> seriesSummaries = new ArrayList<>();
        List<SeriesData> data = seriesRepository.findAllData();
        for(SeriesData foundSeries : data){
            SeriesSummary summary = new SeriesSummary();
            summary.setId(foundSeries.getId());
            summary.setName(foundSeries.getName());
            summary.setDescription(foundSeries.getDescription());
            //tags insert from seriestags-findbyseriesId -> id of tags -> tagrepo-findByid
            List<Tags> tags = new ArrayList<>();
            for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(summary.getId())){
                Optional<Tags> optionalTags = tagRepository.findById(sTag.getTags().getId().intValue());
                if (!optionalTags.isPresent()) throw new TagNotFoundException();
                else tags.add(optionalTags.get());
            }
            summary.setSeriesTags(tags);
            summary.setEpisodes(episodesService.getEpisodesBySeries(summary.getId().intValue()));
            seriesSummaries.add(summary);
        }
        return seriesSummaries;
    }

    @Override
    public SeriesSummary getSeriesById(Integer id) {
        SeriesSummary summary = new SeriesSummary();
        SeriesData data = seriesRepository.findSeriesDataById(id);
        summary.setId(data.getId());
        summary.setName(data.getName());
        summary.setDescription(data.getDescription());
        List<Tags> tags = new ArrayList<>();
        for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(summary.getId())){
            Optional<Tags> optionalTags = tagRepository.findById(sTag.getTags().getId().intValue());
            if (!optionalTags.isPresent()) throw new TagNotFoundException();
            else tags.add(optionalTags.get());
        }
        summary.setSeriesTags(tags);
        summary.setEpisodes(episodesService.getEpisodesBySeries(summary.getId().intValue()));
        return summary;
    }
}
