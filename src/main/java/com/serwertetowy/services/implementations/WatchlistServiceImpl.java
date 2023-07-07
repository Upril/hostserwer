package com.serwertetowy.services.implementations;

import com.serwertetowy.services.WatchlistService;
import com.serwertetowy.services.dto.UserSeriesSummary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {
    @Override
    public List<UserSeriesSummary> getWatchlist(Long id) {
        return null;
    }

    @Override
    public UserSeriesSummary addToWatchlist(Integer seriesId, Integer userId, Integer watchflagId) {
        return null;
    }
}
