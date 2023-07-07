package com.serwertetowy.services;

import com.serwertetowy.services.dto.UserSeriesSummary;

import java.util.List;

public interface WatchlistService {
    List<UserSeriesSummary> getWatchlist(Long id);
    UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId, Integer watchflagId);
}
