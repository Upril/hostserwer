package com.serwertetowy.services.dto;

import com.serwertetowy.entities.WatchFlags;
//dto containing user and series summaries to avoid large file transfers
public interface UserSeriesSummary {
    Long getId();
    SeriesSummary getSeriesSummary();
    UserSummary getUserSummary();
    WatchFlags getWatchflag();
}
