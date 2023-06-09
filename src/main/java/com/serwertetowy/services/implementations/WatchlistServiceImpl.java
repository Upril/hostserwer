package com.serwertetowy.services.implementations;

import com.serwertetowy.entities.*;
import com.serwertetowy.repos.*;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.UserService;
import com.serwertetowy.services.WatchlistService;
import com.serwertetowy.services.dto.SeriesSummary;
import com.serwertetowy.services.dto.UserSeriesData;
import com.serwertetowy.services.dto.UserSeriesSummary;
import com.serwertetowy.services.dto.UserSummary;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {
    @Autowired
    UserSeriesRepository userSeriesRepository;
    @Autowired
    SeriesRepository seriesRepository;
    @Autowired
    EpisodesService episodesService;
    @Autowired
    SeriesTagsRepository seriesTagsRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    WatchFlagRepository watchFlagRepository;
    @Autowired
    UserService userService;

    @Override
    public List<UserSeriesSummary> getWatchlist(Long id) {
        List<UserSeriesData> userSeriesList = userSeriesRepository.findByUserId(id);
        List<UserSeriesSummary> userSeriesSummaryList = new ArrayList<>();
        //convert userseriesdata, which contains only user and series id, into a proper watchlist dto
        for (UserSeriesData userSeries: userSeriesList){
            userSeriesSummaryList.add(new UserSeriesSummary() {
                @Override
                public Long getId() {
                    return userSeries.getId();
                }

                @Override
                public SeriesSummary getSeriesSummary() {
                    SeriesSummary summary = new SeriesSummary();
                    Series series = seriesRepository.findById(userSeries.getSeriesId().intValue()).orElseThrow(()->
                            new ResponseStatusException(HttpStatus.NOT_FOUND));
                    summary.setId(series.getId());
                    summary.setName(series.getName());
                    summary.setDescription(series.getDescription());
                    summary.setEpisodes(episodesService.getEpisodesBySeries(userSeries.getSeriesId().intValue()));
                    List<Tags> tags = new ArrayList<>();
                    for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(series.getId())){
                        Tags foundTags = tagRepository.findById(sTag.getTags().getId().intValue()).orElseThrow(()->
                                new ResponseStatusException(HttpStatus.NOT_FOUND));
                        tags.add(foundTags);
                    }
                    summary.setSeriesTags(tags);
                    return summary;
                }

                @Override
                public UserSummary getUserSummary() {
                    return null;
                }

                @Override
                public WatchFlags getWatchflag() {
                    return watchFlagRepository.findById(userSeries.getWatchFlagsId().intValue()).orElseThrow(()->
                            new ResponseStatusException(HttpStatus.NOT_FOUND));
                }
            });

        }
        return userSeriesSummaryList;
    }

    @Override
    public UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId, Integer watchflagId) {
        User user = userService.getUserById(userId.longValue());
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        //series added to watchlist by default are set to the "Watching" watchflag
        UserSeries userSeries = new UserSeries(user,series,watchFlagRepository.findById(watchflagId)
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
