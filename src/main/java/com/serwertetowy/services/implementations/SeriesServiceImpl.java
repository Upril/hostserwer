package com.serwertetowy.services.implementations;

import com.serwertetowy.entities.*;
import com.serwertetowy.exceptions.TagNotFoundException;
import com.serwertetowy.repos.*;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.dto.*;
import com.serwertetowy.services.SeriesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
public class SeriesServiceImpl implements SeriesService {
    private EpisodesService episodesService;
    private SeriesRepository seriesRepository;
    private SeriesTagsRepository seriesTagsRepository;
    private TagRepository tagRepository;
    private WatchFlagRepository watchFlagRepository;
    private UserSeriesRepository userSeriesRepository;
    private UserService userService;
    @Override
    public Series saveSeries(String name, String description, Set<Tags> tags) {
        //assembling series tag data from request data
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
        //assembling series summary data from the SeriesData dto, containing only id,name and desc
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
            //assembling the series summary - adding the episode summary data without the video file
            summary.setEpisodes(episodesService.getEpisodesBySeries(summary.getId().intValue()));
            seriesSummaries.add(summary);
        }
        return seriesSummaries;
    }

    @Override
    public SeriesSummary getSeriesById(Integer id) {
        //identical as the previous method, but for only 1 seriesgiven by id
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

    //may be changed in the future to allow the user to just ignore a series
    @Override
    public UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId) {
        //adding the series to user watchlist
        User user = userService.getUserById(userId.longValue());
        Series series = seriesRepository.findById(seriesId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        //series added to watchlist by default are set to the "Watching" watchflag
        UserSeries userSeries = new UserSeries(user,series,watchFlagRepository.findById(1).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));
        userSeriesRepository.save(userSeries);
        SeriesSummary seriesSummary = new SeriesSummary();
        seriesSummary.setId(series.getId());
        seriesSummary.setName(series.getName());
        seriesSummary.setDescription(series.getDescription());
        seriesSummary.setEpisodes(episodesService.getEpisodesBySeries(seriesId));
        List<Tags> tags = new ArrayList<>();
        for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(series.getId())){
            Optional<Tags> optionalTags = tagRepository.findById(sTag.getTags().getId().intValue());
            if (!optionalTags.isPresent()) throw new TagNotFoundException();
            else tags.add(optionalTags.get());
        }
        seriesSummary.setSeriesTags(tags);
        return new UserSeriesSummary() {
            @Override
            public Long getId() {
                return userSeries.getId();
            }

            @Override
            public SeriesSummary getSeriesSummary() {
                return seriesSummary;
            }

            @Override
            public UserSummary getUserSummary() {
                return userService.getUserByEmail(user.getEmail());
            }

            @Override
            public WatchFlags getWatchflag() {
                return watchFlagRepository.findById(1).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            }
        };
    }
}
