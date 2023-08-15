package com.serwertetowy.services;

import com.serwertetowy.exceptions.UserDeletedException;
import com.serwertetowy.services.dto.UserSeriesSummary;

import java.util.List;

public interface WatchlistService {
    List<UserSeriesSummary> getWatchlist(Long id, String authIdentity);
    UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId, Integer watchflagId, String authIdentity) throws UserDeletedException;
    void putWatchlistItem(Long id, Integer seriesId, Integer watchflagId, Boolean isFavourite, String authIdentity);
    void deleteWatchlistItem(Long id, String authIdentity);
}
