package com.serwertetowy.services.implementations;

import com.serwertetowy.entities.*;
import com.serwertetowy.repos.*;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.dto.*;
import com.serwertetowy.services.SeriesService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
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
    @Autowired
    private ResourceLoader resourceLoader;
    private Series seriesAssemble(String name, String description, List<Integer> tagIds){
        //assembling series tag data from request data
        Set<SeriesTags> seriesTagsSet = new HashSet<>();
        Series series = new Series(name,description);
        for (Integer id : tagIds){
            seriesTagsSet.add(new SeriesTags(series,tagRepository.findById(id).orElseThrow(
                    ()->new ResponseStatusException(HttpStatus.NOT_FOUND))));
        }
        series.setSeriesTags(seriesTagsSet);
        return series;
    }
    @Override
    public Series saveSeries(String name, String description, List<Integer> tagIds) throws IOException {
        //assembling series tag data from request data
        Series series = seriesAssemble(name,description,tagIds);
        series.setImageData(resourceLoader.getResource("classpath:/images/banner.jpg").getContentAsByteArray());
        seriesRepository.save(series);
        return series;
    }

    @Override
    public Series saveSeriesWithImage(MultipartFile file, String name, String description, List<Integer> tagIds) throws IOException {
        Series series = seriesAssemble(name,description,tagIds);
        series.setImageData(file.getBytes());
        seriesRepository.save(series);
        return series;
    }

    @Override
    @Transactional
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
                Tags foundTags = tagRepository.findById(sTag.getTags().getId().intValue())
                        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
                tags.add(foundTags);
            }
            summary.setSeriesTags(tags);
            //assembling the series summary - adding the episode summary data without the video file
            summary.setEpisodes(episodesService.getEpisodesBySeries(summary.getId().intValue()));
            seriesSummaries.add(summary);
        }
        return seriesSummaries;
    }

    @Override
    @Transactional
    public SeriesSummary getSeriesById(Integer id) {
        //identical as the previous method, but for only 1 seriesgiven by id
        SeriesSummary summary = new SeriesSummary();
        SeriesData data = seriesRepository.findSeriesDataById(id);
        summary.setId(data.getId());
        summary.setName(data.getName());
        summary.setDescription(data.getDescription());
        List<Tags> tags = new ArrayList<>();
        for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(summary.getId())){
            Tags foundTags = tagRepository.findById(sTag.getTags().getId().intValue())
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            tags.add(foundTags);
        }
        summary.setSeriesTags(tags);
        summary.setEpisodes(episodesService.getEpisodesBySeries(summary.getId().intValue()));
        return summary;
    }

    @Override
    @Transactional
    public Mono<Resource> getSeriesImageData(Integer id) {
        ByteArrayResource imageData = new ByteArrayResource(seriesRepository.findById(id).orElseThrow
                (()->new ResponseStatusException(HttpStatus.NOT_FOUND)).getImageData());
        return Mono.fromSupplier(()->imageData);
    }


    //may be changed in the future to allow the user to just ignore a series
    @Override
    public UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId, Integer watchflagId) {
        //adding the series to user watchlist
        User user = userService.getUserById(userId.longValue());
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        //series added to watchlist by default are set to the "Watching" watchflag
        UserSeries userSeries = new UserSeries(user,series,watchFlagRepository.findById(1)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));
        userSeriesRepository.save(userSeries);

        SeriesSummary seriesSummary = new SeriesSummary();
        seriesSummary.setId(series.getId());
        seriesSummary.setName(series.getName());
        seriesSummary.setDescription(series.getDescription());
        seriesSummary.setEpisodes(episodesService.getEpisodesBySeries(seriesId));

        List<Tags> tags = new ArrayList<>();
        for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(series.getId())){
            Tags foundTags = tagRepository.findById(sTag.getTags().getId().intValue())
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            tags.add(foundTags);
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
                return watchFlagRepository.findById(watchflagId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            }
        };
    }
}
