package com.serwertetowy.services.dto;

import com.serwertetowy.entities.WatchFlags;

public interface UserSeriesSummary {
    Long getId();
    SeriesSummary getSeriesSummary();
    UserSummary getUserSummary();
    WatchFlags getWatchflag();
}
